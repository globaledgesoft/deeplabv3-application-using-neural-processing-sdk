# QDN DeepLabV3 Android Application

The project is designed to utilize the Qualcomm Neural Processing SDK, a deep learning software from Qualcomm Snapdragon Platforms. The Neural Processing SDK is used to convert trained models from Caffe, Caffe2, ONNX, TensorFlow to Snapdragon supported format (.dlc format). We further utilize these models in Android application to perform semantic segmentation using DeepLab V3 support in NPE.
## Pre-requisites
* Before starting the Andhttps://github.com/jinka2015/QDN-DeepLabV3-NPE/blob/master/AndroidApplication/app/src/main/res/drawable/snapdragon_hdk.jpgroid application, please follow the instructions for setting up SNPE using the link provided.
	https://developer.qualcomm.com/docs/snpe/setup.html. 
* Android device 6.0 and above which uses below mentioned Snapdragon processors/Snapdragon HDK with display can be used to test the application

## List of Supported Snapdragon Devices

- Qualcomm Snapdragon 855
- Qualcomm Snapdragon 845
- Qualcomm Snapdragon 835
- Qualcomm Snapdragon 821
- Qualcomm Snapdragon 820
- Qualcomm Snapdragon 710
- Qualcomm Snapdragon 660
- Qualcomm Snapdragon 652
- Qualcomm Snapdragon 636
- Qualcomm Snapdragon 630
- Qualcomm Snapdragon 625
- Qualcomm Snapdragon 605
- Qualcomm Snapdragon 450

The above list supports the application with CPU and GPU.For more information on the supported devices, please follow this link https://developer.qualcomm.com/docs/snpe/overview.html

## Components
Below are the items used in the project.
1. Mobile Display with QDN_DeepLabV3_NPE app
2. HDK Snapdragon board with GPU enabled
3. USB type – C cable
4. External camera setup
5. Power Cable

## Hardware Setup
![Qualcomm Snapdragon HDK image](https://github.com/jinka2015/QDN-DeepLabV3-NPE/blob/master/AndroidApplication/app/src/main/res/drawable/snapdragon_hdk.jpg)


## How does it work?
QDN Image Segmentation application opens a camera preview, clicks a picture and converts it to bitmap. The network is built via  Neural Network builder by passing deeplabv3.dlc as the input. The bitmap is then given to model for inference, which returns FloatTensor output. The output is again set for post-processing to achieve background manipulation (changing the background color to black and white) of the original input image.


## Steps to Install and Run the Application
* Firstly set up the hardware as shown above in the hardware setup section
* Power on the Snapdragon HDK board
* Connect the Dev-Board/Android phone via USB to the device
* Switch on the display and choose the USB connection option to File Transfer
* Check if ADB is installed in the windows/linux device, if not follow the below instructions in the below link to install
	https://developer.android.com/studio/command-line/adb.html.
* Use the below command to install the apk with the connected device with help of adb. [Download APK(Debug)](https://github.com/jinka2015/QDN-DeepLabV3-NPE/blob/master/AndroidApplication/output/qdn_segmentation.apk)

	$ adb install qdn_segmentation.apk
* Search the QC Image Segmentation in the app menu and launch the application

## Screenshot of the application
<img src="https://github.com/jinka2015/QDN-DeepLabV3-NPE/blob/master/AndroidApplication/app/src/main/res/drawable/screenshot_segmentation.png" widht=640 height=360 />
