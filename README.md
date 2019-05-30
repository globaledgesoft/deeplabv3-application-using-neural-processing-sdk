# QDN DeepLabV3 Board Application

The application demonstrates the Image semantic Segmentation considering the input image in portrait mode and changes the background to grayscale except for the persons. DeepLabV3 model is used for the Application implementation. DeepLab is an algorithm implemented by Google for the semantic segmentation which will classify the object each pixel by pixel and assign the label for each of them. Image semantic segmentation is helpful in applications like Background change, portrait mode, etc.


To develop a DeepLabV3 model for image segmentation we use Snapdragon mobile platforms(SD835) with the help of Qualcomm's Neural Processing SDK.
## Recommended setup for model training
### Hardware Prerequisite
1. Intel i5 or greater
2. NVIDIA 10 series or greater
3. Ram 16 GB or more

### System Software Requirements
1. Ubuntu 14.04 LTS or above
2. Cuda
3. CuDNN

## How to train the model

Tensorflow’s DeepLab API setup is the prerequisite for training the DeepLabV3 model. 
For installation steps follow the below instruction,

- Execute the following commands for installing the dependencies,

```bash
# Run this command from <PROJECT_ROOT_DIR>
$ sudo pip install -r dependencies/requirement.txt
```
- Update the Python Environment setup

```bash
$ cd <path to tensorflow setup>/tensorflow/models/research
$ export PYTHONPATH=$PYTHONPATH:`pwd`:`pwd`/slim
```
Note: The Environment setup needed to be made for every new terminal session.  To avoid this add above lines at the end of  .bashrc file with an absolute path.

## Testing the Installation

Run the below commands ti test if installation is successful,
```bash
$ cd <path to tensorflow setup>/tensorflow/models/research
$ python deeplab/model_test.py
```
After completion of installation, execute the below commands to start the training,
```bash
$ cd <path to tensorflow setup>/tensorflow/models/research/deeplab
$ sh local_test.sh 
```
Note: By default the iteration count in  local_test.sh is 10, if required kindly modify.

## How to Convert Tensorflow's model into DLC?

Prerequisites: Neural Processing Engine(NPE) SDK setup. Use the instructions in from the below link to make the setup,

https://developer.qualcomm.com/software/qualcomm-neural-processing-sdk/getting-started

- Initialize the environmental variables of Neural Processing SDK with tensorflow.

- The model is trained using TensorFlow framework and exported to graph file with .pb extension.

- Once you’ve .pb file convert it into dlc using the following command:
```bash
$ snpe-tensorflow-to-dlc –graph deeplabv3_mnv2_pascal_train_aug/frozen_inference_graph.pb -i sub_7 1,513,513,3 --out_node ArgMax --dlc deeplabv3.dlc --allow_unconsumed_nodes
```

## Inference On Ubuntu using Qualcomm’s Neural Processing Engine SDK

Qualcomm’s NPE SDK Doesn’t support direct images as an input for the model for inference.
NPE SDK requires the Numpy array which is stored in raw form on secondary storage. In order to run the application in Neural Processing SDK we should first have to do some basic image pre-processing to pass the input to the Neural Process SDK. 

Following are the details of pre-processing steps followed in src/deep_input_raw.py file.

(We have used the opencv for pre-processing the image)
1. Resize the image with the shape of 513x513x3
2. Convert the image type float32 after padding the smaller dimensions to the mean value of 128
3. The padding is used to produce an image of 513x513x3
4. Multiply the image element-wise with 0.00784313771874 and subtract 1.0 respectively
5. Store this pre-processed array as a raw file


On executing the src/deep_input_raw.py script, blob.raw file is generated and the ArgMax:0.raw file generated will be stored in output/Result_0 directory.

# Procedure to change the background using DeepLab

Following is the detailed description of steps followed to change the background for a pre-processed image using src/post_process_deeplab_output.py,

1. The output of the DeepLabV3 model is 513x513x1 numpy array.
2. Read the output file as a float32 
3. Each element having the predicted class number of the corresponding pixels.
4. Replace the background of image resized according to the output of the array predicted
5. Resize the image with original size


post_process_deeplab_output.py script will change the background to grayscale for a pre-processed image.

