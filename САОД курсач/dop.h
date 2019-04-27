#ifndef DOP_H
#define DOP_H
/*
int TreeSize (vertex *p){
    if (!p) return 0;
    return (1 + TreeSize (p -> left) + TreeSize (p -> right));    
}

int Max (int a,int b){
    if (a > b) return a;
    return b;    
}

int Height (vertex *p){
    if (!p) return 0;
    return  (1 + Max (Height (p -> left), Height (p -> right)));
}
*/
void SDP (vertex *&tree, vah V){ // алгоритм добавления в СДП, tree - указатель на корень дерева, D - данные, которые необходимо добавить

    vertex **p = &tree; // указатель на указатель на вершину дерева

    while (*p != NULL){
    	if (strcmp (V.FIO, (*p) -> FIO) < 0) p = &((*p) -> left); // добавление влево
    	else if (strcmp (V.FIO, (*p) -> FIO) > 0) p = &((*p) -> right); // добавление вправо 
    	else break; // данные с ключом уже есть в дереве
	}

	if (*p == NULL){ // создание дерева, если оно было пустым
		(*p) = new vertex;
		strcpy ((*p) -> FIO, V.FIO);
		(*p) -> dep = V.dep;
		strcpy ((*p) -> d, V.d);
		strcpy ((*p) -> law, V.law);
		(*p) -> weight = V.weight;
		(*p) -> right = NULL;
		(*p) -> left = NULL;
	}
}

void dopAlgorithm (vertex *&tree, int l, int r, vah *V){ // алгоритм А2
	int weight = 0, summ = 0;
	int i;
	// l и r - левая и правая границы рабочей части массива
	if (l <= r){

		for (i = l; i < r; i++)
			weight += V[i].weight; //считаем вес всего дерева

		for (i = l; i < r - 1; i++){ //выбираем такую выершину, чтобы разность левого и правого поддеревьев была минимальной
			if (summ < weight/2 && summ + V[i].weight > weight/2)
				break;
			summ += V[i].weight;
		}

		SDP (tree, V[i]); // добавление в СДП
		dopAlgorithm (tree, l, i - 1, V);
		dopAlgorithm (tree, i + 1, r, V);
	}
}

void preBuilding (vertex *&tree, queue *head, vah *&V){
	int n = 0, i, h, size;
	queue *p;

	for (p = head; p; p = p -> next){
		n += 1; // считаем количество вершин дерева
	}

	V = new vah [n]; // объявляем дерево с n вершинами

	p = head; // перемещаем указатель в начало очереди

	for (i = 0; i < n; i++){
		
		strcpy (V[i].FIO, p -> FIO); // копируем все элементы очереди в вершины дерева
		V[i].dep = p -> dep;
		strcpy (V[i].d, p -> d);
		strcpy (V[i].law, p -> law);
	    V[i].weight = rand() % 500 + 1; // задаем случайный вес каждой вершине
	    
		p = p -> next;
	}

	sortForDOP (V, n); // сортируем массив перед построением, используя heap sort

	dopAlgorithm (tree, 0, n - 1, V); // строим ДОП по алгоритму А2
	
	system ("cls");
}

void treeWalk (vertex *p){ //обход дерева
    if (p != 0){ 
        treeWalk (p -> left);
        printf ("%s %d %s %s\n", p -> FIO, p -> dep, p -> d, p -> law);
        treeWalk (p -> right);
    }
}

void treeSearch (vertex *tree){ // поиск в дереве
	char *a; // ключ поиска
	int i, n = 0;
	
	a = new char[30];
	
	memset (a, '\0', 30); // заполнение ключа поиска нулями
	printf ("Enter a key: ");
	gets (a); // ввод ключа поиска
	memset (a, '\0', 30);
	gets (a);
	
	for (i = 0; i < 30; i++) //проверяем длину введенного ключа
		if (a[i] == '\0') break;
	
	n = i; //длина искомого слова
	
    while (tree != NULL){
        if (strncmp (tree -> FIO, a, n) < 0) tree = tree -> right; //если ключ поиска больше текущего корня, идем в правое поддерево
        else if (strncmp (tree -> FIO, a, n) > 0) tree = tree -> left; //если ключ поиска меньше текущего корня, идем в левое поддерево
        else break;
    }
    
    if (tree != NULL) printf ("%s %d %s %s\n", tree -> FIO, tree -> dep, tree -> d, tree -> law); //если элменет был найдем - выводим его 
    else printf ("Not found!\n"); // если элемент не был найден
    system ("pause");
    system ("cls");
}

#endif
