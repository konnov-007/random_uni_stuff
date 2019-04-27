#ifndef BINSEARCH_H
#define BINSEARCH_H

int binarySearch (record *depositers, int n, char *searchKey){ // �������� �����, ������ 2
	int l, r, flag = 0, m, border;
	// l � r - ����� � ������ ������� ������� ����� �������
	// flag - ����������, ������� �������� ���� ��������� ���������� ������
	l = 0;
	r = n - 1;

	while (l < r){
		m = (l + r) / 2;
 		if (strncmp (depositers[m].law, searchKey, 3) < 0) l = m + 1; //���������� ������ ��� ������� ������ depositers[m].law � ������ ������ searchKey. ���������� 0 ���� �������� ����� ����� ��������� �����, ���������� �������� ������ ����, ���� ������  depositers[m].law ������ ������ searchKey, �������� ������ ���� ��������������� �� ��������
  			else r = m;
	}
	
	if (strncmp (depositers[r].law, searchKey, 3) == 0) flag = 1; //���� depositers[r].law ��������� � ������ ������ �������� ���������� flag = 1 (��� �������� ������� ������)
 		else flag = 0; 

 	if (flag){ // ���� ������� ������
		border = r; // ����������� ���������� border ����� ����� ��������� �������
		do {
			printf (" %s %d %s %s\n", depositers[r].FIO, depositers[r].dep, depositers[r].d, depositers[r].law);
			r += 1;
		} while (strcmp (depositers[r].law, depositers[r + 1].law) == 0);
	}

	else printf ("Not found!");

	system ("pause");
	system ("cls");
	if (flag) return border; //����������� ������ ������ �������� �� ���������
	else return -1;
}

int inputKeyForBinSearch (record *depositers, int n){ // ������� ��� ����� ����� ������
	char searchKey[4]; // ���� ������
	int border; // ������ ������� ������� ����� �������

	memset (searchKey, '\0', 4); // ���������� 4� ������ ����� ������, ����� ��������� searchKey, 
//"searchKey" - ��������� �� ���� ������ ��� ����������, 
//'\0' - ���������� � ���� ������ �����, �� ������� ��������� ���� ������ ��������, ���������� ���� ������ � �����
	system ("cls");
	printf ("Enter three letters of a lawyer lastname: ");
	scanf ("%s", &searchKey); //���� ����� ������

	border = binarySearch (depositers, n, searchKey); // ����� �� ����� "�" (��� ��������)
	return border;
}

#endif
