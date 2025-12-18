package com.adrian.repostory;

import java.util.List;

import com.adrian.model.Cliente;

public interface ClienteRepository {
    List<Cliente> listar();
    void guardar(Cliente t);
}
