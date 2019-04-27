#ifndef SORT_H
#define SORT_H

/*
int compare (record *depositers, int pos, int j){ // функци€ сравнени€
    if (strcmp (depositers[pos].law, depositers[j].law) < 0)
        return 1;
	
	else if (strcmp(depositers[pos].law, depositers[j].law) > 0)
        return 0;
	
	else if (strcmp(depositers[pos].law, depositers[j].law) == 0){
        if (depositers[pos].dep < depositers[j].dep)
            return 1;
		
		else if (depositers[pos].dep > depositers[j].dep)
            return 0;
    }
    
    return -1;
}
*/



void digitalSort(record *depositers){
	record *arrayPointer[4000]; // создание указател€ на массив структур
	for (int i = 0; i < 4000; ++i) arrayPointer[i] = &depositers[i]; // заполнение массива указателей адресами элементов массива, хран€щего базу данных 
	// —ам алгоритм сортировки
	record *massive[256][2000]; 
    int k = 1;
    int counter[256]; // количество очередей
    for (int rank = 0; rank < 8; ++rank) { // сортировка по полю сумма вклада
        for (int i = 0; i < 10; ++i) 
            counter[i] = 0;
        for (int i = 0; i < 4000; ++i) { // разбиваем по 256 очеред€м
            massive[arrayPointer[i]->dep / k % 10][counter[arrayPointer[i]->dep / k % 10]] = arrayPointer[i]; 
            counter[arrayPointer[i]->dep / k % 10]++;
        }
        int arrayNumber = 0;
        int inxNumber = 0;
        for (int i = 0; i < 10; ++i) {
            while (arrayNumber != counter[i]) { //возвращаем отсортированные элементы обратно в исходный массив
                arrayPointer[inxNumber] = massive[i][arrayNumber];
                arrayNumber++;
                inxNumber++;
            }
            arrayNumber = 0;
        }
        k *= 10;
    }
    for (int rank = 21; rank >= 0; --rank) { // сортировка по полю ‘»ќ адвоката
        for (int i = 0; i < 256; ++i)
            counter[i] = 0;
        for (int i = 0; i < 4000; ++i) { // разбиваем по 256 очеред€м
            int tmp = (int)(arrayPointer[i]->law[rank]);
            tmp = tmp < 0 ? tmp + 128 : tmp;
            massive[tmp][counter[tmp]]= arrayPointer[i];
            counter[tmp]++;
        }
        int arrayNumber = 0;
        int inxNumber = 0;
        for (int i = 0; i <= 255; ++i) { //возвращаем отсортированные элементы обратно в исходный массив
            while (arrayNumber != counter[i]) {
                arrayPointer[inxNumber] = massive[i][arrayNumber];
                arrayNumber++;
                inxNumber++;
            }
            arrayNumber = 0;
        }
    }    
	
	// ¬озвращение отсортированной базы данных в массив depositers
	record *temp;
	temp = new record[N];
	for (int i = 0; i < N; ++i) 
		temp[i] = *arrayPointer[i];
		
	for (int i = 0; i < N; ++i) 
		depositers[i] = temp[i];

}





void pyramidForDOP (vah *depositers, int l, int r){
	int i, j;
	vah x;

	strcpy (x.FIO, depositers[l-1].FIO);
	x.dep = depositers[l-1].dep;
	strcpy (x.d, depositers[l-1].d);
	strcpy (x.law, depositers[l-1].law);
	x.weight = depositers[l-1].weight;
	
	i = l - 1;

	while (1){
 		j = 2 * i;
 		if (j > r) break;
 		if (j < r && (strcmp (depositers[j+1].FIO, depositers[j].FIO) == 1 || strcmp (depositers[j+1].FIO, depositers[j].FIO) == 0)) j+=1;
 		if (strcmp (x.FIO, depositers[j].FIO) == 1 || strcmp (x.FIO, depositers[j].FIO) == 0) break;

		strcpy (depositers[i].FIO, depositers[j].FIO);
		depositers[i].dep = depositers[j].dep;
		strcpy (depositers[i].d, depositers[j].d);
		strcpy (depositers[i].law, depositers[j].law);
		depositers[i].weight = depositers[j].weight;

 		i = j;
 	}

 	strcpy (depositers[i].FIO, x.FIO);
	depositers[i].dep = x.dep;
	strcpy (depositers[i].d, x.d);
	strcpy (depositers[i].law, x.law);
	depositers[i].weight = x.weight;
}

void sortForDOP (vah *depositers, int n){ // сортировка массива дл€ ƒќѕ перед построением дерева
	int l, r; // лева€ и права€ границы
	vah k; // указатель на дерево

	l = n / 2; 

	while(l > 0){
 		pyramidForDOP (depositers, l, n - 1); // построение (l, n-1) пирамиды
 		l -= 1;
 	}

	r = n - 1;

	while (r >= 1){
		strcpy (k.FIO, depositers[0].FIO);
		k.dep = depositers[0].dep;
		strcpy (k.d, depositers[0].d);
		strcpy (k.law, depositers[0].law);
		k.weight = depositers[0].weight;

		strcpy (depositers[0].FIO, depositers[r].FIO);
		depositers[0].dep = depositers[r].dep;
		strcpy (depositers[0].d, depositers[r].d);
		strcpy (depositers[0].law, depositers[r].law);
		depositers[0].weight = depositers[r].weight;

 		strcpy (depositers[r].FIO, k.FIO);
		depositers[r].dep = k.dep;
		strcpy (depositers[r].d, k.d);
		strcpy (depositers[r].law, k.law);
		depositers[r].weight = k.weight;

 		r -= 1;

 		pyramidForDOP (depositers, 1, r); // построение (1, r) пирамиды
 	}
}

#endif
