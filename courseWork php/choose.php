<HTML>
<?php
    $HOST = "localhost";
    $USER = "root";
    $PASS = "root";
    $TABLE = "t1";
    $DATABASE = "sample";

    $link = mysqli_connect("$HOST", "$USER", "$PASS");
    mysqli_select_db($link, $DATABASE) or die ("Нельзя открыть $DATABASE");

    $result = mysqli_query($link, "SELECT * FROM $TABLE");
    $num_fields = mysqli_num_fields($result);

    print "<form action='print.php' method=GET >\n";
    print "<TABLE>";

  for($i = 0; $i < $num_fields; $i++)
    {
        $name = mysqli_fetch_field_direct($result,$i)->name;
        print "<tr>";
        print "<th>".$name."</th>";
        print "<td align=center><input type=checkbox name=column[] value=".$name."></td>"; // вариант илья п
//        print "<td align=center><input type=radio name=column[] value=".$name."></td>"; // вариант илья к
        print "</tr>";
    }
/* // выпадающий список
echo "<select name = ''>";
    for($i = 0; $i < $num_fields; $i++)
    {
        $name = mysqli_fetch_field_direct($result,$i);

       // echo "<option value = '$name->column_name' > $name->column_name </option>";
//        print "<option name = column[] value = column[] > $name->name </option>";
//        print "<option name = column[] value = column[] > $name->name </option>";

        print "<option name = column[] value = > $name->name </option>";


    }
    echo "</select>";
    */


    print "</TABLE>";
    print "\t <input type='submit' value='Вывести'>";
    print "</form>";
?>
</HTML>
