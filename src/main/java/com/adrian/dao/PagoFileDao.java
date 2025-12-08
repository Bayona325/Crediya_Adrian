package com.adrian.dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.adrian.model.Pago;

public class PagoFileDao implements GenericDao<Pago> {
    private final File file;
    public PagoFileDao(String path) {
        this.file = new File(path);
    }
    @Override
    public List<Pago> listar() throws Exception {
        List<Pago> res = new ArrayList<>();
        if (!file.exists()) return res;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine())!=null) {
                String[] p = line.split("\t");
                if (p.length<4) continue;
                Pago pago = new Pago();
                pago.setId(Integer.valueOf(p[0]));
                pago.setPrestamoId(Integer.valueOf(p[1]));
                pago.setFechaPago(LocalDate.parse(p[2]));
                pago.setMonto(Double.valueOf(p[3]));
                res.add(pago);
            }
        }
        return res;
    }
    @Override
    public void guardar(Pago p) throws Exception {
        Integer nextId = 1;
        for (var ex: listar()) if (ex.getId()!=null && ex.getId()>=nextId) nextId = ex.getId()+1;
        p.setId(nextId);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
            bw.write(String.join("\t", String.valueOf(p.getId()), String.valueOf(p.getPrestamoId()), p.getFechaPago().toString(), String.format("%.2f", p.getMonto())));
            bw.newLine();
        }
    }
}
