package com.adrian.dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import com.adrian.model.Cliente;

public class ClienteFileDao implements GenericDao<Cliente> {
    private final File file;
    public ClienteFileDao(String path) {
        this.file = new File(path);
    }

    @Override
    public List<Cliente> listar() throws Exception {
        List<Cliente> res = new ArrayList<>();
        if (!file.exists()) return res;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split("\t");
                if (p.length<5) continue;
                Cliente c = new Cliente(Integer.valueOf(p[0]), p[1], p[2], p[3], p[4]);
                res.add(c);
            }
        }
        return res;
    }
    @Override
    public void guardar(Cliente c) throws Exception {
        Integer nextId = 1;
        List<Cliente> all = listar();
        for (Cliente ex: all) if (ex.getId()!=null && ex.getId()>=nextId) nextId = ex.getId()+1;
        c.setId((nextId));
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
            bw.write(String.join("\t", String.valueOf(c.getId()), c.getNombre(), c.getDocumento(), c.getCorreo(), c.getTelefono()));
            bw.newLine();
        }
    }
}