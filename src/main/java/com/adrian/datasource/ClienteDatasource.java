package com.adrian.datasource;

import java.util.List;

import com.adrian.model.Cliente;

public interface ClienteDatasource {
     List<Cliente> listar();
    void guardar(Cliente t);
}
