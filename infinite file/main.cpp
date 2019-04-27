#include <stdio.h>
#include <time.h>
#include <conio.h>

main(){
	clock_t process;
	
	FILE *output = fopen("output.txt", "w");
	FILE *result = fopen("result.txt", "a");
	if(result == NULL)
		FILE *result = fopen("result.txt", "w");
		
	process=clock();
	char a = 'a';
	int i=0;
	printf("Press any key to stop the test...\n\n");
	while(!kbhit())
	{
		fprintf(output ,"%c", a);
		i++;
	}
	process = clock() - process;
	fclose(output);
	double outputTime = (double)process/CLOCKS_PER_SEC; 
	printf("There were %d letters 'a' by", i);
	printf(" %2.3f", outputTime);
	printf(" seconds.");
	printf("\n\n%d / %2.3f = %lf", i, outputTime, (double)i/outputTime );
	fprintf(result, "There were %d letters 'a' by", i);
	fprintf(result, " %2.3f", outputTime);
	fprintf(result, " seconds.");
	fprintf(result, "\n%d / %2.3f = %lf\n\n", i, outputTime, (double)i/outputTime );
	getch();
	getch();
	printf("\n\nPress any key to exit...");
	getch();
	return 0;
}
