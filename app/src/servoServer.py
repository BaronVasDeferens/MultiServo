import RPi.GPIO as GPIO
import bluetooth
import ast

name = "bt_server"
target_name = "test"
uuid = "00001101-0000-1000-8000-00805F9B34FB"

x = 0

servoPin1 = 24
servoPin2 = 16
servoPin3 = 12
servoPin4 = 25
servoPin5 = 23


def runServer():

    global x
    global servoPin1
    global servoPin2
    global servoPin3
    global servoPin4
    global servoPin5
    

    GPIO.setwarnings(False)
    GPIO.setmode(GPIO.BCM)
    
    GPIO.setup(servoPin1, GPIO.OUT)
    GPIO.setup(servoPin2, GPIO.OUT)
    GPIO.setup(servoPin3, GPIO.OUT)
    GPIO.setup(servoPin4, GPIO.OUT)
    GPIO.setup(servoPin5, GPIO.OUT)
    
    pwm1 = GPIO.PWM(servoPin1, 50)
    pwm1.start(5)

    pwm2 = GPIO.PWM(servoPin2, 50)
    pwm2.start(5)

    pwm3 = GPIO.PWM(servoPin3, 50)
    pwm3.start(5)

    pwm4 = GPIO.PWM(servoPin4, 50)
    pwm4.start(5)

    pwm5 = GPIO.PWM(servoPin5, 50)
    pwm5.start(5)

    serverSocket = bluetooth.BluetoothSocket(bluetooth.RFCOMM)
    port = bluetooth.PORT_ANY
    serverSocket.bind(("", port))
    print "Servo Server"
    print "Listening for connections on port: ", port

    while 1:
        # wait for a message to be sent
        serverSocket.listen(0)
        port = serverSocket.getsockname()[1]

        bluetooth.advertise_service(serverSocket, "BluetoofServer",
                                    service_id=uuid,
                                    service_classes=[uuid, bluetooth.SERIAL_PORT_CLASS],
                                    profiles=[bluetooth.SERIAL_PORT_PROFILE]
                                    )

        inputSocket, address = serverSocket.accept()
        print "Got connection with ", address
        while 1:
            try:
                data = inputSocket.recv(1024)
                data = data.split("}")[0] + "}"
                data = dict(ast.literal_eval(data))
                
                servo = data["servo"]; 
                pos = data["position"]
              
                print(str(servo) + " : " + str(pos))                

                if (servo == "s1"):
                   targetPwm = pwm1
		if (servo == "s2"):
		   targetPwm = pwm2
                if (servo == "s3"):
		   targetPwm = pwm3
		if (servo == "s4"):
		   targetPwm = pwm4
		if (servo == "s5"):
		   targetPwm = pwm5
                
		targetPwm.ChangeDutyCycle(convertToDegrees(pos))
                
                   
            except bluetooth.btcommon.BluetoothError:
                print("Exception thrown: quitting...")
                break

        #inputSocket.close()
        #serverSocket.close()


def convertToDegrees(val):
    if (val < 0):
        val = 0
    if (val > 650)
        val = 650
    inMin = 0
    # effective edge of screen is 9 and 1013
    inMax = 1013.0
    outMin = 1.0
    outMax = 11.0
    # return (val - inMin) * (outMax - outMin) // (inMax - inMin) + outMin
    return ((val / inMax) * 9.0) + outMin

def dutyCycle(val):
    return 1 / 18 * val + 2


runServer()
