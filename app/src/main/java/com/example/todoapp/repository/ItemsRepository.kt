package com.example.todoapp.repository

import com.example.todoapp.room.TodoItem
import com.example.todoapp.room.TodoListDatabase
import kotlinx.coroutines.flow.Flow


class ItemsRepository(
    db: TodoListDatabase
) {
    /*init {
        data["1"] = TodoItem("1", "Простая задачка без дедлайна", Importance.REGULAR, null,false, 1586913715141,"Заглушка")
        data["2"] = TodoItem("2", "Срочная задачка без дедлайна", Importance.URGENT, null, true,1586913715141,"Заглушка")
        data["3"] = TodoItem("3", "Не обязательная задачка без дедлайна", Importance.LOW, null, false,1586913715141,"Заглушка")
        data["4"] = TodoItem("4", "Простая задачка с дедлайном", Importance.REGULAR, Date(1585913715141),false, 1586913715141,"Заглушка")
        data["5"] = TodoItem("5", "Срочная задачка с дедлайном", Importance.URGENT, Date(1586913715141),false, 1586913715141,"Заглушка")
        data["6"] = TodoItem("6", "Не обязательная задачка с дедлайном", Importance.LOW, Date(1584913715141), false,1586913715141,"Заглушка")
        data["7"] = TodoItem("7", "Срочная задачка с дедлайном выполненная", Importance.URGENT, Date(1584913515141),true, 1586913715141,"Заглушка")
        data["8"] = TodoItem("8", "Обычная задачка без дедлайна выполненная", Importance.REGULAR, null,true, 1586913715141,"Заглушка")
        data["9"] = TodoItem("9", "Не обязательная задачка без дедлайна выполненная", Importance.LOW, null, true,1586913715141,"Заглушка")
        data["10"] = TodoItem("10", "Очень длинная задачка для скролла второго экрана без дедлайна. Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum. Contrary to popular belief, Lorem Ipsum is not simply random text. It has roots in a piece of classical Latin literature from 45 BC, making it over 2000 years old. Richard McClintock, a Latin professor at Hampden-Sydney College in Virginia, looked up one of the more obscure Latin words, consectetur, from a Lorem Ipsum passage, and going through the cites of the word in classical literature, discovered the undoubtable source. Lorem Ipsum comes from sections 1.10.32 and 1.10.33 of \"de Finibus Bonorum et Malorum\" (The Extremes of Good and Evil) by Cicero, written in 45 BC. This book is a treatise on the theory of ethics, very popular during the Renaissance. The first line of Lorem Ipsum, \"Lorem ipsum dolor sit amet..\", comes from a line in section 1.10.32.\n" +
                "\n" +
                "The standard chunk of Lorem Ipsum used since the 1500s is reproduced below for those interested. Sections 1.10.32 and 1.10.33 from \"de Finibus Bonorum et Malorum\" by Cicero are also reproduced in their exact original form, accompanied by English versions from the 1914 translation by H. Rackham.",
            Importance.REGULAR, null,false, 1586913715141,"Заглушка")
        data["11"] = TodoItem("11", "Очень длинная задачка для скролла второго экрана с дедлайном. Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum. Contrary to popular belief, Lorem Ipsum is not simply random text. It has roots in a piece of classical Latin literature from 45 BC, making it over 2000 years old. Richard McClintock, a Latin professor at Hampden-Sydney College in Virginia, looked up one of the more obscure Latin words, consectetur, from a Lorem Ipsum passage, and going through the cites of the word in classical literature, discovered the undoubtable source. Lorem Ipsum comes from sections 1.10.32 and 1.10.33 of \"de Finibus Bonorum et Malorum\" (The Extremes of Good and Evil) by Cicero, written in 45 BC. This book is a treatise on the theory of ethics, very popular during the Renaissance. The first line of Lorem Ipsum, \"Lorem ipsum dolor sit amet..\", comes from a line in section 1.10.32.\n" +
                "\n" +
                "The standard chunk of Lorem Ipsum used since the 1500s is reproduced below for those interested. Sections 1.10.32 and 1.10.33 from \"de Finibus Bonorum et Malorum\" by Cicero are also reproduced in their exact original form, accompanied by English versions from the 1914 translation by H. Rackham.",
            Importance.LOW, Date(1584913315141),false, 1586913715141,"Заглушка")
        data["12"] = TodoItem("12", "Сварить суп", Importance.URGENT, null, false,1586913715141,"Заглушка")
        data["13"] = TodoItem("13", "Доделать проект", Importance.REGULAR, Date(1584912315141),false, 1586913515141,"Заглушка")
        data["14"] = TodoItem("14", "Залить на гит этот замечательный to do list", Importance.URGENT, null,false, 1586913715141,"Заглушка")

        data["15"] = TodoItem("15", "Еще больше супа", Importance.URGENT, null, false,1586913415141,"Заглушка")
        data["16"] = TodoItem("16", "Доделать задачки в Яндекс контесте", Importance.URGENT, null,false, 1586912715141,"Заглушка")
        data["17"] = TodoItem("17", "Радоваться жизни", Importance.URGENT, Date(1584913345141),false, 1586913315141,"Заглушка")
        //numDone = 1
    }*/

    private val dao = db.listDao

    fun getData(all: Boolean): Flow<List<TodoItem>> {
        return when (all) {
            true -> dao.getAll()
            false -> dao.getToDo()
        }
    }

    fun getItem(itemId: String): Flow<TodoItem> = dao.getItem(itemId)

    suspend fun addItem(todoItem: TodoItem) {
        dao.add(todoItem)
    }

    suspend fun deleteItem(todoItem: TodoItem) {
        dao.delete(todoItem)
    }

    suspend fun changeItem(newItem: TodoItem) {
        dao.update(newItem)
    }

    suspend fun changeDone(id: String, done: Boolean) {
        dao.updateDone(id, done)
    }


}