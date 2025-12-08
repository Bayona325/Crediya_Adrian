package com.adrian.dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import com.adrian.model.Empleado;

public class EmpleadoFileDao implements GenericDao<Empleado> {
    private final File file;
    public EmpleadoFileDao(String path) {
        this.file = new File(path);
    }
    @Override
    public List<Empleado> listar() throws Exception {
        List<Empleado> res = new ArrayList<>();
        if (!file.exists()) return res; 
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\t");
                if (parts.length<6) continue;
                Empleado e = new Empleado(Integer.valueOf(parts[0]), parts[1], parts[2], parts[3], parts[4], Double.valueOf(parts[5]));
                res.add(e);
            }
        }
        return res;
    }
    @Override
    public void guardar(Empleado e) throws Exception {
        Integer nextId = 1;
        List<Empleado> all = listar();
        for (Empleado ex: all) if (ex.getId()!=null && ex.getId()>=nextId) nextId = ex.getId()+1;
        e.setId(nextId);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
            bw.write(String.join("\t", String.valueOf(e.getId()), e.getNombre(), e.getDocumento(), e.getRol(), e.getCorreo(), String.format("%.2f", e.getSalario())));
            bw.newLine();
        }
    }
}
