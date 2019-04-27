#ifndef QUEUE_H
#define QUEUE_H

void printList (queue *head){ // ����� �������
	queue *p;

	for (p = head; p; p = p -> next){
		printf ("%s %d %s %s\n", p -> FIO, p -> dep, p -> d, p -> law);
	}

	system ("pause");
	system ("cls");
}

void formQueue (queue *&head, record *depositers, int border){ // �������� �������
	queue *p; 
	queue *tail; // ��������� �� ����� �������

	head = new queue;

	head -> next = NULL;

	tail = head;

	do {
		p = new queue;
		//�������� �������� ������� � �������
		strcpy (p -> FIO, depositers[border].FIO);
		p -> dep = depositers[border].dep;
		strcpy (p -> d, depositers[border].d);
		strcpy (p -> law, depositers[border].law);

		p -> next = NULL;

		tail -> next = p;
		tail = p;

		border += 1;
	} while (strcmp (depositers[border].law, depositers[border + 1].law) == 0);
	// �������� �������
	printList (head -> next);
}







#endif
