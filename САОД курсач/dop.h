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
void SDP (vertex *&tree, vah V){ // �������� ���������� � ���, tree - ��������� �� ������ ������, D - ������, ������� ���������� ��������

    vertex **p = &tree; // ��������� �� ��������� �� ������� ������

    while (*p != NULL){
    	if (strcmp (V.FIO, (*p) -> FIO) < 0) p = &((*p) -> left); // ���������� �����
    	else if (strcmp (V.FIO, (*p) -> FIO) > 0) p = &((*p) -> right); // ���������� ������ 
    	else break; // ������ � ������ ��� ���� � ������
	}

	if (*p == NULL){ // �������� ������, ���� ��� ���� ������
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

void dopAlgorithm (vertex *&tree, int l, int r, vah *V){ // �������� �2
	int weight = 0, summ = 0;
	int i;
	// l � r - ����� � ������ ������� ������� ����� �������
	if (l <= r){

		for (i = l; i < r; i++)
			weight += V[i].weight; //������� ��� ����� ������

		for (i = l; i < r - 1; i++){ //�������� ����� ��������, ����� �������� ������ � ������� ����������� ���� �����������
			if (summ < weight/2 && summ + V[i].weight > weight/2)
				break;
			summ += V[i].weight;
		}

		SDP (tree, V[i]); // ���������� � ���
		dopAlgorithm (tree, l, i - 1, V);
		dopAlgorithm (tree, i + 1, r, V);
	}
}

void preBuilding (vertex *&tree, queue *head, vah *&V){
	int n = 0, i, h, size;
	queue *p;

	for (p = head; p; p = p -> next){
		n += 1; // ������� ���������� ������ ������
	}

	V = new vah [n]; // ��������� ������ � n ���������

	p = head; // ���������� ��������� � ������ �������

	for (i = 0; i < n; i++){
		
		strcpy (V[i].FIO, p -> FIO); // �������� ��� �������� ������� � ������� ������
		V[i].dep = p -> dep;
		strcpy (V[i].d, p -> d);
		strcpy (V[i].law, p -> law);
	    V[i].weight = rand() % 500 + 1; // ������ ��������� ��� ������ �������
	    
		p = p -> next;
	}

	sortForDOP (V, n); // ��������� ������ ����� �����������, ��������� heap sort

	dopAlgorithm (tree, 0, n - 1, V); // ������ ��� �� ��������� �2
	
	system ("cls");
}

void treeWalk (vertex *p){ //����� ������
    if (p != 0){ 
        treeWalk (p -> left);
        printf ("%s %d %s %s\n", p -> FIO, p -> dep, p -> d, p -> law);
        treeWalk (p -> right);
    }
}

void treeSearch (vertex *tree){ // ����� � ������
	char *a; // ���� ������
	int i, n = 0;
	
	a = new char[30];
	
	memset (a, '\0', 30); // ���������� ����� ������ ������
	printf ("Enter a key: ");
	gets (a); // ���� ����� ������
	memset (a, '\0', 30);
	gets (a);
	
	for (i = 0; i < 30; i++) //��������� ����� ���������� �����
		if (a[i] == '\0') break;
	
	n = i; //����� �������� �����
	
    while (tree != NULL){
        if (strncmp (tree -> FIO, a, n) < 0) tree = tree -> right; //���� ���� ������ ������ �������� �����, ���� � ������ ���������
        else if (strncmp (tree -> FIO, a, n) > 0) tree = tree -> left; //���� ���� ������ ������ �������� �����, ���� � ����� ���������
        else break;
    }
    
    if (tree != NULL) printf ("%s %d %s %s\n", tree -> FIO, tree -> dep, tree -> d, tree -> law); //���� ������� ��� ������ - ������� ��� 
    else printf ("Not found!\n"); // ���� ������� �� ��� ������
    system ("pause");
    system ("cls");
}

#endif
