package com.example.todo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.*;
import com.example.todo.model.Todo;
import com.example.todo.repository.TodoRepository;
import com.example.todo.model.TodoRowMapper;

@Service
public class TodoH2Service implements TodoRepository {
    @Autowired
    private JdbcTemplate db;

    @Override

    public ArrayList<Todo> getTodos() {
        return (ArrayList<Todo>) db.query("select * from todolist", new TodoRowMapper());
    }

    @Override

    public Todo addTodo(Todo todo) {
        db.update("insert into todolist (todo,status,priority) values(?,?,?)", todo.getTodo(), todo.getStatus(),
                todo.getPriority());
        return db.queryForObject("select * from todolist where todo=? and status=?", new TodoRowMapper(),
                todo.getTodo(),
                todo.getStatus());
    }

    @Override

    public Todo getTodoById(int id) {
        try {
            return db.queryForObject("select * from todolist where id=?", new TodoRowMapper(), id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override

    public Todo updateTodo(int id, Todo todo) {
        if (todo.getTodo() != null) {
            db.update("update todolist set todo=? where id=?", todo.getTodo(), id);
        }
        if (todo.getStatus() != null) {
            db.update("update todolist set status=? where id=?", todo.getStatus(), id);
        }
        if (todo.getPriority() != null) {
            db.update("update todolist set priority=? where id=?", todo.getPriority(), id);
        }
        return getTodoById(id);
    }

    @Override

    public void deleteTodo(int id) {
        db.update("delete form todolist where id=?", id);
    }

}