<?php
    $HOST = "localhost";
    $USER = "root";
    $PASS = "root";
    $TABLE = "t1";
    $DATABASE = "sample";

    $link = mysqli_connect("$HOST", "$USER", "$PASS");
    mysqli_select_db($link, $DATABASE) or die ("Нельзя открыть $DATABASE");

    if(!isset($_GET['column']))
    {
        $result = mysqli_query($link,"SELECT * FROM $TABLE");
        $num_rows = mysqli_num_rows($result);
        $num_fields = mysqli_num_fields($result);

        print "<p><TABLE border=\"1\">\n";
        print "\t<tr>\n";
        for($i = 0; $i < $num_fields; $i++)
        {
            $name = mysqli_fetch_field_direct($result,$i)->name;
            print "\t\t<th>$name</th>\n";
        }

        while ($a_row = mysqli_fetch_row($result))
        {
            print "\t<tr>\n";
            foreach ($a_row as $field)
                print "\t\t<td>$field</td>\n";
            print "\t</tr>\n";
        }
        print "</TABLE>\n";
    }else{
        foreach($_GET['column'] as $column)
        {
            print "<h4>Содержимое столбца $column</h4>";

            print "<TABLE border=1>";
            print "\t<th> $column </th>\n";

            $result = mysqli_query($link, "SELECT $column FROM $TABLE") or die(mysql_error());

            while($a_row = mysqli_fetch_row($result))
            {
                print "\t<tr>";
                foreach($a_row as $field)
                    echo"<td>$field</td>";
                print "</tr>\n"	;
            }
            print "</TABLE>";
        }
    }
    print "<br><a href='choose.php'>Назад</a>";
?>
