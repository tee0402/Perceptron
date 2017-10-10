package com.company;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Perceptron {
    public static void main(String[] args) {
        int width = 1000;
        int height = 1000;
        EZ.initialize(width,height);
        EZ.addLine(0, height / 2, width, height / 2, Color.BLACK, 1);
        EZ.addLine(width / 2, 0, width / 2, height, Color.BLACK, 1);
        int n = 208; //change

        double[][] inputs = new double[n][2];
        int[] labels = new int[n];
        double[] weights = new double[3];

        try {
            Scanner scanner = new Scanner(new File("test1.txt")); //change
            for (int i = 0; i < n; i++) {
                inputs[i][0] = Double.valueOf(scanner.next()) / 10;
                inputs[i][1] = Double.valueOf(scanner.next()) / 10;

                if (Integer.valueOf(scanner.next()) == 0) {
                    labels[i] = -1;
                    EZ.addCircle((int)(inputs[i][0] * width / 2 + width / 2), (int)(height / 2 - inputs[i][1] * height / 2), 10,10, Color.BLUE, true);
                } else {
                    labels[i] = 1;
                    EZ.addCircle((int)(inputs[i][0] * width / 2 + width / 2), (int)(height / 2 - inputs[i][1] * height / 2), 10,10, Color.RED, true);
                }
            }
            scanner.close();
            EZ.refreshScreen();
        }
        catch (IOException e) {
            System.out.println("IOException");
        }

        weights[0] = 0;
        weights[1] = 0;
        weights[2] = 0;
        double totalError, sum, y, error;
        double c = 0.5;
        int iterations = 1;
        EZLine line = EZ.addLine(0,(int)(height / 2 - (weights[0] * -1 + weights[2]) / -weights[1] * height / 2), width, (int)(height / 2 - (weights[0] + weights[2]) / -weights[1] * height / 2), Color.BLACK, 3);;

        do {
            totalError = 0;
            EZText iterationText = EZ.addText(width / 2, height / 20, "Iteration " + iterations + ":", Color.BLACK, 40);
            for (int i = 0; i < n; i++) {
                sum = inputs[i][0] * weights[0] + inputs[i][1] * weights[1] + weights[2];
                if (sum >= 0) {
                    y = 1;
                }
                else {
                    y = -1;
                }
                //error = l - y
                //error = 0 if l - y = 0, error = 2 or -2 otherwise
                error = labels[i] - y;
                //w = w + c * (l - y) * x
                weights[0] += c * error * inputs[i][0];
                weights[1] += c * error * inputs[i][1];
                weights[2] += c * error;
                //totalError = 0 if error = 0 for all points, error is squared so all errors added are positive, so negative errors do not cancel out positive errors
                totalError += error * error;
                if (error != 0 || i == n - 1) {
                    line = EZ.addLine(0,(int)(height / 2 - (weights[0] * -1 + weights[2]) / -weights[1] * height / 2), width, (int)(height / 2 - (weights[0] + weights[2]) / -weights[1] * height / 2), Color.BLACK, 3);
                    EZ.refreshScreen();
                }
                //Don't erase line if it has converged(no error for all points), erase line otherwise
                if (!(i == n - 1 && totalError == 0)) {
                    EZ.removeEZElement(line);
                }
            }
            if (totalError != 0) {
                EZ.removeEZElement(iterationText);
                EZ.refreshScreen();
            }
            System.out.println("Iteration " + iterations + ":");
            System.out.println("weight 1: " + weights[0]);
            System.out.println("weight 2: " + weights[1]);
            System.out.println("weight 3: " + weights[2]);
            iterations++;
        } while (totalError != 0 && iterations < 1000);
        EZ.addText(width / 2, height / 10, "Converged", Color.GREEN, 40);
    }
}
