#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <math.h>
#include "base.h"
#include "binsearch.h"
#include "queue.h"
#include "sort.h"
#include "dop.h"
#include "gilbertMur.h"

int main(){
	int key = 0, border = 0; //key - перменная для выбора меню в switch, border - правая граница рабочей части массива при бинпоиске
	record *depositers; // указатель на структуру данных
	queue *head; // указатель на начало очереди 
	vertex *tree = NULL; // указатель на вершину дерева СДП
	vah *V; // указатель на вершину дерева ДОП
	
	depositers = new record[N]; // создание нового массива структур записей
	
	readTheBase (depositers); // считывание базы
	
	do{ // вывод меню
		printf ("1. PRINT BASE\n");
        printf ("2. SORT THE ARRAY VIA DIGITAL SORT\n");
        printf ("3. SEARCH IN THE BASE\n");
        printf ("4. PRINT THE QUEUE\n");
        printf ("5. BUILD A DOP TREE\n");
        printf ("6. PRINT THE DOP TREE\n");
        printf ("7. SEARCH IN THE DOP TREE\n");
        printf ("8. GILBERT MUR ENCODING\n");
        printf ("0. EXIT\n");
        printf ("Input: ");
		scanf ("%d", &key);
		system("cls");

		switch(key){
			case 1: printTheBase (depositers); break; //печатаем базу
			case 2: digitalSort (depositers); break; // сортируем цифровой сортировкой
			case 3: border = inputKeyForBinSearch (depositers, N); break; // ищем двоичным поиском по фамилии адвоката
			case 4: formQueue (head, depositers, border); break; // формируем очередь из найденных элементов
			case 5: preBuilding (tree, head -> next, V); break; // строим ДОП дерево
			case 6: treeWalk (tree); system ("pause"); system ("cls"); break; // обход дерева и вывод
			case 7: treeSearch (tree); break; // поиск вершины в дереве
			case 8: startCode (); break; // кодирование базы методом Гилберта-Мура
			case 0: return 0; // выход из программы 
			default: break; 
		}

	}while(1);

    return 0;
}
