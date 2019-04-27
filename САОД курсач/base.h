#ifndef BASE_H // метод условной компиляции
#define BASE_H
#include <conio.h> // библиотека для работы с getch()

const int N = 4000;

struct vah{ // структура корня дерева для работы с доп
	char FIO[30];
	unsigned short int dep;
	char d[10];
	char law[22];
	int weight; // вес вершины 
};

struct vertex { // структура вершины дерева для работы с сдп
	char FIO[30];
	unsigned short int dep;
	char d[10];
	char law[22];
	int weight;
	vertex *right;
	vertex *left;
};

struct record { // структура записи
	char FIO[30]; // ФИО вкладчика
	unsigned short int dep; // сумма вклада
	char d[10]; // дата
	char law[22]; // ФИО адвоката
};

struct queue { // структура очереди
	char FIO[30];
	unsigned short int dep;
	char d[10];
	char law[22];
	queue *next;
};

void readTheBase (record *depositers){ // считывание базы
	int i; // создание счетчика
	FILE *fp; // указатель на переменную типа FILE для работы с файлом
	
	fp = fopen ("testbase3.dat", "rb"); // открывание двоичного файла для чтения
	
	for (i = 0; i < N; i++){ // считывание всех N (4000) объектов
		fread (&depositers[i], sizeof(depositers), 16, fp); // помещаем содержимое файла в массив depositers, каждый объект по sizeof(depositers) символов в длину (размер элемента массива в байтах), количество элементов - 16
		                                                    //sizeof(depositers) = 4, size_t Count = 16 потому что 64 = 16*4. То есть считывание массив размером в 16 элементов, каждый из которых sizeof(depositers) байт из потока, и сохраняем его в блоке памяти, на который указывает depositers
	
	}
	
}

void printTheBase (record *depositers){ // функция вывода базы
	int startNumber = 0; // начальное число (используется для того, чтобы выводить 20 элементов по порядку)
	char key = 0; // переменная типа char для использования функции getch (getch используется для выхода из цикла вывода посредством кнопки escape)
	while(1){ // цикл до того, пока не нажат escape
		for (int i = startNumber; i <startNumber + 20; ++i) // печатаем 20 записей каждый раз
			printf ("%d) %s %d %s %s\n", i + 1, depositers[i].FIO, depositers[i].dep, depositers[i].d, depositers[i].law); //выводим индекс записи, ФИО вкладчика, сумма вклада, дата и ФИО адвоката
	
		if (startNumber + 20 == N) { // если все 4000 записей распечатаны - выходим в главное меню
    		printf ("\nEnd of DB. Press any key to return to menu\n\n");
    		return;
		}
	
		printf ("\nPress ESC to exit\nOR press any key except ESC to print 20 more elements\n\n"); // ожидание нажатия любой кнопки для вывода следующих 20 записей
		key = _getch(); // считывание нажатой кнопки
	    if (key == 27) { //в случае если нажатая кнопка - escape, мы завершаем вывод и выходим обратно в главное меню
        	key = 0;
        	return;
    	}
    	/*if (key == 'w'){
    		if (startNumber != 0)
    			startNumber = startNumber - 20;
    	}
    	else*/
   		startNumber = startNumber + 20; // приращение перменной startNumber для вывода следующих 20 элементов
	}
}

#endif
