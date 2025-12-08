package com.adrian.dao;

import java.util.List;

public interface GenericDao<T> {
   List<T> listar() throws Exception;
   void guardar(T t) throws Exception;
}