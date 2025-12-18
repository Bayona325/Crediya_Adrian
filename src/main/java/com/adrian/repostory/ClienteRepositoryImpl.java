package com.adrian.repostory;

import java.util.List;

import com.adrian.datasource.ClienteDatasource;
import com.adrian.model.Cliente;

public class ClienteRepositoryImpl implements ClienteRepository{
    private ClienteDatasource local;
    private ClienteDatasource db;
    public ClienteRepositoryImpl(ClienteDatasource local, ClienteDatasource db) {
        this.db = db;
        this.local = local;
    }

    @Override
    public List<Cliente> listar() {
        return local.listar();
    }

    @Override
    public void guardar(Cliente t) {
        db.guardar(t);
        local.guardar(t);
    }
    
}
