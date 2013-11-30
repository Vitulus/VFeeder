<?php 
function date_diff($date1, $date2) { 

    $current = $date1; 
    $datetime2 =$date2; 
    
    $count = 0; 
    while(date_create($current) < $datetime2){ 
        $current = gmdate("Y-m-d", strtotime("+1 day", strtotime($current))); 
        $count++; 
    } 
    return strval($count); 
} 

//echo $count;
//echo (date_diff('2010-3-9', '2011-4-10')." days <br \>"); 
?>