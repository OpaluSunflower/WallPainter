import socket
import threading

class SocketController:

    def __init__(self, port,adress):
        self.port = port
        self.adress = adress
        self.socket = None

    def createsocket(self):
        s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        s.bind((self.adress, self.port))
        self.socket = s

    def recivephoto(self):
        self.socket.listen()
        conn, addr = self.socket.accept()
        with conn:
            while True:
                data = conn.recv(1024)
                f = open("demofile2.png", "wb")
                f.write(data)
                f.close()
                if not data:
                    break
