# QDN DeepLabV3 Board Application

The application demonstrates the Image semantic segmentation. DeepLabV3 is used in this application, which is an algorithm implemented by Google for semantic segmentation.

This will classify the object in each pixel from given portrait image and assigns the label to each of them. Image semantic segmentation is helpful in applications like background change, portrait mode, etc.

To develop a DeepLabV3 model for image segmentation we use Snapdragon mobile platforms(SD835) and Qualcomm's Neural Processing Engine(NPE) SDK.
## Recommended setup for model training
### Hardware prerequisite
1. Intel i5 or greater
2. NVIDIA 10 series or greater
3. Ram 16 GB or more

### System software requirements
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
- Clone the tensorflow and its model repositories  by executing the below commands,

```bash
$ git clone https://github.com/tensorflow/tensorflow.git
$ cd tensorflow
$ git clone https://github.com/tensorflow/models.git
```
- Update the Python Environment setup

```bash
$ cd <path to tensorflow setup>/tensorflow/models/research
$ export PYTHONPATH=$PYTHONPATH:`pwd`:`pwd`/slim
```
Note: The environment setup needed to be made for every new terminal session.

To avoid updating the environment path for every new session, add above lines at the end of .bashrc file with an absolute path.

## Testing the Installation

Run the below commands to test if installation is successful,
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

## How to convert Tensorflow's model into DLC?

Prerequisites: Neural Processing Engine SDK setup. Use the instructions from the below link to make the setup,

https://developer.qualcomm.com/software/qualcomm-neural-processing-sdk/getting-started

- Initialize the environmental variables of NPE SDK with tensorflow.

- The model is trained using TensorFlow framework and exported to graph file with .pb extension.
    
- Once you’ve .pb file convert it into .dlc using the following command:
```bash
$ snpe-tensorflow-to-dlc –graph deeplabv3_mnv2_pascal_train_aug/frozen_inference_graph.pb -i sub_7 1,513,513,3 --out_node ArgMax --dlc deeplabv3.dlc --allow_unconsumed_nodes
```

## Inference on Ubuntu using Qualcomm’s Neural Processing Engine SDK

Qualcomm’s NPE SDK doesn’t support direct images as an input to the model for inference.

NPE SDK requires the NumPy array stored as a raw file. In order to run the application in NPE SDK we need to preprocess the input image. 

Following are the details of pre-processing steps followed in src/deep_input_raw.py file,

(We have used the opencv for pre-processing the image)
1. Resize the image with the dimensions of 513x513x3.
2. Pad the smaller dimension with the mean value of 128 to produce an image of 513x513x3.
3. Convert the image to type float32.
4. Multiply the image element-wise with 0.00784313771874 and subtract 1.0 respectively.
5. Store this pre-processed array as a raw file (blob.raw).

On executing the src/deep_input_raw.py script, blob.raw file is generated.


# Procedure to change the background using DeepLab

Following is the detailed description of steps followed to change the background for a pre-processed image using src/post_process_deeplab_output.py,

1. The output (output/Result_0/ArgMax:0.raw) from the previous section is a NumPy array of dimension 513x513x1.
2. Each element in the array contains the predicted class index of the corresponding pixels for the given input image.
3. The index number of person is 15.
4. Read the NumPy array into a data buffer of type float32.
5. To change the background to grayscale, the pixel values are assigned such that R, G, and B components have identical values.
   For example, the pixel values R(123), G(93) and  B(49)  are modified to R(123), G(123), B(123).
6. Loop through the NumPy array and change the pixel values in the original resized image (as per step 5 above) other than pixels of class index 15.
7. Resize the image to the original size.

post_process_deeplab_output.py script will change the background to grayscale for a pre-processed image.

