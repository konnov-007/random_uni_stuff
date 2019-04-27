#ifndef GILBERTMUR_H
#define GILBERTMUR_H

#include <iostream>
#include <math.h>
#include <iomanip>

const int n = 256; // ���������� �������� ��������� �������� 
unsigned long int number; // �����
float entropy; // ��������
float averageLength; // ������� �����
struct codeStruct { 
    unsigned char a;
    float p; // �����������
    char codeWord[50]; // ���������� �����
    int l;
}codeArray[n];




void openFile() {
    int i, j = 0;
    unsigned char ch;
    FILE *f = fopen("testBase3.dat", "rb"); //��������� ���� ��� ������
    for (i = 0; i < n; i++) {
        codeArray[i].a = i; // ����� �������
        codeArray[i].p = (float)(0);
        codeArray[i].l = 0;
    }
    while (!feof(f)) { //������� ���������� �������� � ����� � �� �������������
        fscanf(f, "%c", &ch);
        codeArray[ch].p += 1.0; //��������� � ������, �������� ������ "ch" +1 � ��������� � ++number - ����� ���-�� ���������
        number++;
    }
    fclose(f);
    for (i = 0; i < n; i++) // ������� ����������� ������� �������
        if (codeArray[i].p != (float)(0)) {
            codeArray[i].p /= (float)number; //���� ���� ������� ����, ����� ���, ����� �������� ����������� ���������
            j++;
        }
    number = (unsigned long int)(j);
}
void deleteBlank() { // ��������� ������� �����������
    int i, j, k;
    codeStruct temp;
    for (i = 0; i < n - 1; i++) {
        k = 1;
        if (codeArray[i].p == (float)(0)) // ���� ����� ����, �� ������ � �����
            for (j = i + 1; j < n; j++)
                if (k)
                    if (codeArray[j].p != (float)(0)) {
                        temp = codeArray[i];
                        codeArray[i] = codeArray[j];
                        codeArray[j] = temp;
                        k = 0;
                    }
    }
    i = 0;
    while (codeArray[i].p != 0.0) i++;
    number = (unsigned long int)(i);
}
void gilbertMurCode() {
    float q[n], s = 0.0; // ��� q - ������������� �����������
    int i, j = 0;
    q[0] = codeArray[0].p / 2;
    codeArray[0].l = (int)(-log(codeArray[0].p) / log(2) + 2);
    s = codeArray[0].p;
    for (i = 1; i < number; i++) { // ������� ������������ ����������� � ����� �������� �����
        q[i] = s + codeArray[i].p / 2;
        codeArray[i].l = (int)(-log(codeArray[i].p) / log(2) + 2);
        s += codeArray[i].p;
    }
    for (i = 0; i < number; i++) // ������� � �������� ������� ���������
        for (j = 0; j < codeArray[i].l; j++) {
            q[i] *= 2;
            codeArray[i].codeWord[j] = (char)(q[i]);
            if (q[i] > 1) q[i]--;
        }
    for (i = 0; i < number; i++) { // �������� � ������� �����
        entropy += -codeArray[i].p * log(codeArray[i].p) / log(2);
        averageLength += codeArray[i].l * codeArray[i].p;
    }
}
void codePrint() {
    system("cls");
    int i, j;
    float check = 0;
    for (i = 0; i < number; i++) {
        std::cout.precision(6);
        std::cout.setf(std::ios::fixed);
        std::cout << std::setw(1) << (char)codeArray[i].a << " "
                  << (float)codeArray[i].p << " " << std::setw(2)
                  << (int)codeArray[i].l << " ";
        for (j = 0; j < codeArray[i].l; ++j)
            std::cout << (int)codeArray[i].codeWord[j];
        std::cout << std::endl;
    }
    std::cout << std::endl;
    std::cout << std::setw(8) << entropy << " - Entropy" << std::endl;
    std::cout << std::setw(8) << averageLength << " - Average length"
              << std::endl;
    std::cout << std::setw(8) << entropy + 2 << " > " << averageLength
              << std::endl
              << std::endl;
    for (int i = 0; i < number; ++i) check += (float)codeArray[i].p;
    std::cout << "Sum of all chances: " << check << std::endl
              << std::endl;
    std::cout << "Press any key to return..." << std::endl;

}
void startCode(){
    number = 0;
    entropy = (float)(0);
    averageLength = (float)(0);
    openFile(); // ��������� ����
    deleteBlank(); // ��������� ������� ����������� (��� �������, ������� �����������, ������� � ������?)
    gilbertMurCode(); // �������� ����� �������� ����
    codePrint(); // ������� ���������
    FILE *code = fopen("code.txt", "wb"); //������� ���� ��� ������
    for (int i = 0; i < number; i++) {
        for (int j = 0; j < codeArray[i].l; j++)
            fprintf(code, "%d", codeArray[i].codeWord[j]); //������� ������� �����
        fprintf(code, "\r\n");
    }
    fclose(code);
}

#endif

