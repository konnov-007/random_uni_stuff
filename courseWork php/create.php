<?php
    $HOST = "localhost";
    $USER = "root";
    $PASS = "root";
    $TABLE = "t1";
    $DATABASE = "sample";
    $COLUMN_ID = "id";
    $COLUMN_NAME = "name";
    $COLUMN_TYPE = "type";
    $COLUMN_FIRM = "firm";

    $link = mysqli_connect("$HOST", "$USER", "$PASS");
    mysqli_select_db($link, $DATABASE) or die ("Нельзя открыть $DATABASE");

    mysqli_query($link, "DROP TABLE IF EXISTS $TABLE");

    $query = "CREATE TABLE $TABLE (
      $COLUMN_ID INT NOT NULL AUTO_INCREMENT, PRIMARY KEY(id),
      $COLUMN_NAME VARCHAR(50) NOT NULL,
      $COLUMN_TYPE VARCHAR(50) NOT NULL,
      $COLUMN_FIRM VARCHAR(50) NOT NULL
    )";

//    mysqli_query($link, $query); // не нужно т.к. мы вызываем подобную функцую в if ниже

    if ($link->query($query) === TRUE) {
    echo "Таблица $TABLE создана";
    } else {
        echo "Нельзя создать таблицу $TABLE: " . $link->error;
    }

    mysqli_query($link, "INSERT INTO $TABLE ($COLUMN_ID, $COLUMN_NAME, $COLUMN_TYPE, $COLUMN_FIRM) VALUES (1,'Access','Реляц','Microsoft')");
    mysqli_query($link, "INSERT INTO $TABLE ($COLUMN_ID, $COLUMN_NAME, $COLUMN_TYPE, $COLUMN_FIRM) VALUES (2,'FoxPro','Реляц','Microsoft')");
    mysqli_query($link, "INSERT INTO $TABLE ($COLUMN_ID, $COLUMN_NAME, $COLUMN_TYPE, $COLUMN_FIRM) VALUES (3,'Oracle7','Реляц','Oracle')");
    mysqli_query($link, "INSERT INTO $TABLE ($COLUMN_ID, $COLUMN_NAME, $COLUMN_TYPE, $COLUMN_FIRM) VALUES (4,'Orion3','ОО','Orion')");
    mysqli_query($link, "INSERT INTO $TABLE ($COLUMN_ID, $COLUMN_NAME, $COLUMN_TYPE, $COLUMN_FIRM) VALUES (5,'Orion4','ОО','Orion')");
    mysqli_query($link, "INSERT INTO $TABLE ($COLUMN_ID, $COLUMN_NAME, $COLUMN_TYPE, $COLUMN_FIRM) VALUES (6,'Delphi','ОО','Microsoft')");
    mysqli_query($link, "INSERT INTO $TABLE ($COLUMN_ID, $COLUMN_NAME, $COLUMN_TYPE, $COLUMN_FIRM) VALUES (7,'Essbase1','Многом','Arbor')");
    mysqli_query($link, "INSERT INTO $TABLE ($COLUMN_ID, $COLUMN_NAME, $COLUMN_TYPE, $COLUMN_FIRM) VALUES (8,'Essbase2','Многом','Arbor')");
    mysqli_query($link, "INSERT INTO $TABLE ($COLUMN_ID, $COLUMN_NAME, $COLUMN_TYPE, $COLUMN_FIRM) VALUES (9,'Orion5','Многом','Orion')");
    mysqli_query($link, "INSERT INTO $TABLE ($COLUMN_ID, $COLUMN_NAME, $COLUMN_TYPE, $COLUMN_FIRM) VALUES (10,'Oracle8','Многом','Oracle')");


    mysqli_close($link);
?>
