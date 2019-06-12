'''Qualcomm Neural Processing SDK Doesn’t support direct images as an input for the model for inferencing.
	This SDK requires the Numpy array which is stored in raw form on secondary storage. 
	In order to run the application in Neural Processing SDK we should firstly have to do 
	some basic image pre-processing to pass the input to the SDK.'''


# Library Import
import numpy as np
import cv2
import os
import argparse


parser = argparse.ArgumentParser()
parser.add_argument("--img_path", metavar="ImagePath", help="Give image path to 
							change the background", required=True, type=str)

args = parser.parse_args()

class PreprocessingInputImage:
	def __init__(self):
		self.img_path = args.img_path
		if((os.path.isfile(self.img_path)) == False):
			print("File not found!")
	def preprocess_image(self):
		print (self.img_path)
		frame = cv2.imread(self.img_path)
		frame_resized = cv2.resize(frame,(513,513)) # resize frame for prediction
		blob = cv2.dnn.blobFromImage(frame_resized, 0.007843, (513, 513), 
												(127.5, 127.5, 127.5), swapRB=True)
		blob = np.reshape(blob, (1,513,513,3))
		np.ndarray.tofile(blob, open('blob.raw','w'))


if __name__ == '__main__':
    convert_img = PreprocessingInputImage()
    convert_img.preprocess_image()


