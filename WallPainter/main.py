
import socket
import socketController

def print_hi(name):
    # Use a breakpoint in the code line below to debug your script.
    print(f'Hi, {name}')  # Press Ctrl+F8 to toggle the breakpoint.


# Press the green button in the gutter to run the script.
if __name__ == '__main__':
    print_hi('PyCharm')
    socketcontroller = socketController(9000,"127.0.0.1")
    socketcontroller.createsocket()
    socketcontroller.recivephoto()



# See PyCharm help at https://www.jetbrains.com/help/pycharm/
