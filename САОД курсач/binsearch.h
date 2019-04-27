#ifndef BINSEARCH_H
#define BINSEARCH_H

int binarySearch (record *depositers, int n, char *searchKey){ // двоичный поиск, версия 2
	int l, r, flag = 0, m, border;
	// l и r - левая и правая границы рабочей части массива
	// flag - переменная, которая отмечает факт успешного завершения поиска
	l = 0;
	r = n - 1;

	while (l < r){
		m = (l + r) / 2;
 		if (strncmp (depositers[m].law, searchKey, 3) < 0) l = m + 1; //сравниваем первые три символа строки depositers[m].law с ключом поиска searchKey. Возвращает 0 если значение обоих строк полностью равны, возвращает значение больше нуля, если строка  depositers[m].law больше строки searchKey, значение меньше нуля свидетельствует об обратном
  			else r = m;
	}
	
	if (strncmp (depositers[r].law, searchKey, 3) == 0) flag = 1; //если depositers[r].law совпадает с ключом поиска присваем переменной flag = 1 (что означает элемент найден)
 		else flag = 0; 

 	if (flag){ // если элемент найден
		border = r; // присваеваем переменной border самый левый найденный элемент
		do {
			printf (" %s %d %s %s\n", depositers[r].FIO, depositers[r].dep, depositers[r].d, depositers[r].law);
			r += 1;
		} while (strcmp (depositers[r].law, depositers[r + 1].law) == 0);
	}

	else printf ("Not found!");

	system ("pause");
	system ("cls");
	if (flag) return border; //возвращение самого левого элемента из найденных
	else return -1;
}

int inputKeyForBinSearch (record *depositers, int n){ // функция для ввода ключа поиска
	char searchKey[4]; // ключ поиска
	int border; // правая граница рабочей части массива

	memset (searchKey, '\0', 4); // заполнение 4х байтов блока памяти, через указатель searchKey, 
//"searchKey" - указатель на блок памяти для заполнения, 
//'\0' - передается в виде целого числа, но функция заполняет блок памяти символом, преобразуя этот символ в число
	system ("cls");
	printf ("Enter three letters of a lawyer lastname: ");
	scanf ("%s", &searchKey); //ввод ключа поиска

	border = binarySearch (depositers, n, searchKey); // поиск по ключу "а" (ФИО адвоката)
	return border;
}

#endif
