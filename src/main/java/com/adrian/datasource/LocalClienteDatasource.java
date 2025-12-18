package com.adrian.datasource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import com.adrian.model.Cliente;

public class LocalClienteDatasource implements ClienteDatasource{

     private final File file;
    public LocalClienteDatasource(String path) {
        this.file = new File(path);
    }

    @Override
    public List<Cliente> listar() {
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
        } catch(Exception e) {
            System.out.println(e.getMessage());
        } 
        return res;
    }

    @Override
    public void guardar(Cliente t) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'guardar'");
    }
    
}
