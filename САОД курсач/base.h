#ifndef BASE_H // ����� �������� ����������
#define BASE_H
#include <conio.h> // ���������� ��� ������ � getch()

const int N = 4000;

struct vah{ // ��������� ����� ������ ��� ������ � ���
	char FIO[30];
	unsigned short int dep;
	char d[10];
	char law[22];
	int weight; // ��� ������� 
};

struct vertex { // ��������� ������� ������ ��� ������ � ���
	char FIO[30];
	unsigned short int dep;
	char d[10];
	char law[22];
	int weight;
	vertex *right;
	vertex *left;
};

struct record { // ��������� ������
	char FIO[30]; // ��� ���������
	unsigned short int dep; // ����� ������
	char d[10]; // ����
	char law[22]; // ��� ��������
};

struct queue { // ��������� �������
	char FIO[30];
	unsigned short int dep;
	char d[10];
	char law[22];
	queue *next;
};

void readTheBase (record *depositers){ // ���������� ����
	int i; // �������� ��������
	FILE *fp; // ��������� �� ���������� ���� FILE ��� ������ � ������
	
	fp = fopen ("testbase3.dat", "rb"); // ���������� ��������� ����� ��� ������
	
	for (i = 0; i < N; i++){ // ���������� ���� N (4000) ��������
		fread (&depositers[i], sizeof(depositers), 16, fp); // �������� ���������� ����� � ������ depositers, ������ ������ �� sizeof(depositers) �������� � ����� (������ �������� ������� � ������), ���������� ��������� - 16
		                                                    //sizeof(depositers) = 4, size_t Count = 16 ������ ��� 64 = 16*4. �� ���� ���������� ������ �������� � 16 ���������, ������ �� ������� sizeof(depositers) ���� �� ������, � ��������� ��� � ����� ������, �� ������� ��������� depositers
	
	}
	
}

void printTheBase (record *depositers){ // ������� ������ ����
	int startNumber = 0; // ��������� ����� (������������ ��� ����, ����� �������� 20 ��������� �� �������)
	char key = 0; // ���������� ���� char ��� ������������� ������� getch (getch ������������ ��� ������ �� ����� ������ ����������� ������ escape)
	while(1){ // ���� �� ����, ���� �� ����� escape
		for (int i = startNumber; i <startNumber + 20; ++i) // �������� 20 ������� ������ ���
			printf ("%d) %s %d %s %s\n", i + 1, depositers[i].FIO, depositers[i].dep, depositers[i].d, depositers[i].law); //������� ������ ������, ��� ���������, ����� ������, ���� � ��� ��������
	
		if (startNumber + 20 == N) { // ���� ��� 4000 ������� ����������� - ������� � ������� ����
    		printf ("\nEnd of DB. Press any key to return to menu\n\n");
    		return;
		}
	
		printf ("\nPress ESC to exit\nOR press any key except ESC to print 20 more elements\n\n"); // �������� ������� ����� ������ ��� ������ ��������� 20 �������
		key = _getch(); // ���������� ������� ������
	    if (key == 27) { //� ������ ���� ������� ������ - escape, �� ��������� ����� � ������� ������� � ������� ����
        	key = 0;
        	return;
    	}
    	/*if (key == 'w'){
    		if (startNumber != 0)
    			startNumber = startNumber - 20;
    	}
    	else*/
   		startNumber = startNumber + 20; // ���������� ��������� startNumber ��� ������ ��������� 20 ���������
	}
}

#endif
