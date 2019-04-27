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
	int key = 0, border = 0; //key - ��������� ��� ������ ���� � switch, border - ������ ������� ������� ����� ������� ��� ���������
	record *depositers; // ��������� �� ��������� ������
	queue *head; // ��������� �� ������ ������� 
	vertex *tree = NULL; // ��������� �� ������� ������ ���
	vah *V; // ��������� �� ������� ������ ���
	
	depositers = new record[N]; // �������� ������ ������� �������� �������
	
	readTheBase (depositers); // ���������� ����
	
	do{ // ����� ����
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
			case 1: printTheBase (depositers); break; //�������� ����
			case 2: digitalSort (depositers); break; // ��������� �������� �����������
			case 3: border = inputKeyForBinSearch (depositers, N); break; // ���� �������� ������� �� ������� ��������
			case 4: formQueue (head, depositers, border); break; // ��������� ������� �� ��������� ���������
			case 5: preBuilding (tree, head -> next, V); break; // ������ ��� ������
			case 6: treeWalk (tree); system ("pause"); system ("cls"); break; // ����� ������ � �����
			case 7: treeSearch (tree); break; // ����� ������� � ������
			case 8: startCode (); break; // ����������� ���� ������� ��������-����
			case 0: return 0; // ����� �� ��������� 
			default: break; 
		}

	}while(1);

    return 0;
}
