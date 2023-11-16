package com.wp;

import com.wp.storage.StorageFileNotFoundException;
import com.wp.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.*;
import java.net.DatagramSocket;
import java.net.MalformedURLException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

@Controller
public class FileUploadController {

    private final StorageService storageService;

    @Value("${pythonadress}")
    private String adress;

    @Value("${pythonsocket}")
    private String port;


    @Autowired
    public FileUploadController(StorageService storageService) {
        this.storageService = storageService;
        System.out.println(this.adress);
    }

    @GetMapping("/")
    public String listUploadedFiles(Model model) throws IOException {
        /*
        model.addAttribute("files", storageService.loadAll().map(
                        path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                                "serveFile", path.getFileName().toString()).build().toUri().toString())
                .collect(Collectors.toList()));*/
        //return "uploadForm";
        System.out.println("No siema");
        return "index";
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        System.out.println("Filename:" + filename);
        Resource file = storageService.loadAsResource(filename);

        if (file == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @GetMapping("/assets/bootstrap/{javaorcss}/{filename}")
    @ResponseBody
    public ResponseEntity<Resource> returnResources(@PathVariable String filename, @PathVariable String javaorcss) throws MalformedURLException {
        System.out.println("Resources: "+filename + javaorcss);
        Path file = Paths.get("src/main/resources/templates/assets/bootstrap/"+javaorcss+"/"+filename);
        Resource resource = new UrlResource(file.toUri());
        return ResponseEntity.ok().body(resource);
    }

    @GetMapping("/assets/{javaorcss}/{filename}")
    @ResponseBody
    public ResponseEntity<Resource> returnResources1(@PathVariable String javaorcss, @PathVariable String filename) throws MalformedURLException {
        System.out.println("Resourceseee: "+javaorcss + filename);
        Path file = Paths.get("src/main/resources/templates/assets/"+javaorcss+"/"+filename);
        Resource resource = new UrlResource(file.toUri());
        return ResponseEntity.ok().body(resource);
    }

    @PostMapping("/")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, @RequestParam("color") String color,
                                   RedirectAttributes redirectAttributes){
        System.out.println(file.getOriginalFilename());
        System.out.println(color);
        storageService.store(file);
        try(Socket socket = new Socket("127.0.0.1",11000)){
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            DataInputStream in = new DataInputStream(socket.getInputStream());
            //System.out.println("Wysłem");
            out.write("**file**".getBytes());
            out.write(file.getBytes());
            out.write("**color**".getBytes());
            out.write(color.getBytes());
            out.write("\r\n".getBytes());
            out.flush();
            redirectAttributes.addFlashAttribute("file_path",storageService.store(in));
        }
        catch (Exception e){
            System.out.println(e.getCause());
        }
        return "redirect:/";
    }


    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}
