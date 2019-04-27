#ifndef GILBERTMUR_H
#define GILBERTMUR_H

#include <iostream>
#include <math.h>
#include <iomanip>

const int n = 256; // количество символов исходного алфавита 
unsigned long int number; // номер
float entropy; // энтропия
float averageLength; // средняя длина
struct codeStruct { 
    unsigned char a;
    float p; // вероятность
    char codeWord[50]; // кодируемое слово
    int l;
}codeArray[n];




void openFile() {
    int i, j = 0;
    unsigned char ch;
    FILE *f = fopen("testBase3.dat", "rb"); //открываем файл для чтения
    for (i = 0; i < n; i++) {
        codeArray[i].a = i; // номер символа
        codeArray[i].p = (float)(0);
        codeArray[i].l = 0;
    }
    while (!feof(f)) { //считаем количество символов в файле и их встречаемость
        fscanf(f, "%c", &ch);
        codeArray[ch].p += 1.0; //добавляем в ячейку, значащую символ "ch" +1 к появления и ++number - общее кол-во элементво
        number++;
    }
    fclose(f);
    for (i = 0; i < n; i++) // считаем вероятность встречи символа
        if (codeArray[i].p != (float)(0)) {
            codeArray[i].p /= (float)number; //если этот элемент есть, делим его, чтобы получить вероятность появления
            j++;
        }
    number = (unsigned long int)(j);
}
void deleteBlank() { // отсеиваем нулевые вероятности
    int i, j, k;
    codeStruct temp;
    for (i = 0; i < n - 1; i++) {
        k = 1;
        if (codeArray[i].p == (float)(0)) // если равен нулю, то пихаем в конец
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
    float q[n], s = 0.0; // где q - коммулятивная вероятность
    int i, j = 0;
    q[0] = codeArray[0].p / 2;
    codeArray[0].l = (int)(-log(codeArray[0].p) / log(2) + 2);
    s = codeArray[0].p;
    for (i = 1; i < number; i++) { // подсчет комулятивной вероятности и длины кодового слова
        q[i] = s + codeArray[i].p / 2;
        codeArray[i].l = (int)(-log(codeArray[i].p) / log(2) + 2);
        s += codeArray[i].p;
    }
    for (i = 0; i < number; i++) // перевод в двоичную систему счисления
        for (j = 0; j < codeArray[i].l; j++) {
            q[i] *= 2;
            codeArray[i].codeWord[j] = (char)(q[i]);
            if (q[i] > 1) q[i]--;
        }
    for (i = 0; i < number; i++) { // энтропия и средняя длина
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
    openFile(); // открываем файл
    deleteBlank(); // отсеиваем нулевые вероятности (все символы, которые встречаются, смещаем в начало?)
    gilbertMurCode(); // кодируем кодом Гилберта Мура
    codePrint(); // Выводим результат
    FILE *code = fopen("code.txt", "wb"); //создаем файл для вывода
    for (int i = 0; i < number; i++) {
        for (int j = 0; j < codeArray[i].l; j++)
            fprintf(code, "%d", codeArray[i].codeWord[j]); //выводим кодовые слова
        fprintf(code, "\r\n");
    }
    fclose(code);
}

#endif

