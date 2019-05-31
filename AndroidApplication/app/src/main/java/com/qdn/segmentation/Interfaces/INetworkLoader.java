package com.qdn.segmentation.Interfaces;

import com.qualcomm.qti.snpe.NeuralNetwork;

public interface INetworkLoader {
    void onNetworkBuilt(NeuralNetwork neuralNetwork);
}
