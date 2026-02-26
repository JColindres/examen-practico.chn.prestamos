package com.chn.prestamos.service;

import com.chn.prestamos.dto.*;
import com.chn.prestamos.entity.Cliente;
import com.chn.prestamos.mapper.ClienteMapper;
import com.chn.prestamos.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository repository;

    public ClienteResponse crear(ClienteRequest request) {

        repository.findByNumeroIdentificacion(request.getNumeroIdentificacion())
                .ifPresent(c -> {
                    throw new RuntimeException("Ya existe un cliente con ese número de DPI");
                });

        Cliente cliente = ClienteMapper.toEntity(request);

        return ClienteMapper.toResponse(repository.save(cliente));
    }

    public List<ClienteResponse> listar() {
        return repository.findAll()
                .stream()
                .map(ClienteMapper::toResponse)
                .collect(Collectors.toList());
    }

    public ClienteResponse actualizar(Integer id, ClienteRequest request) {

        Cliente cliente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        cliente.setNombre(request.getNombre());
        cliente.setApellido(request.getApellido());
        cliente.setDireccion(request.getDireccion());
        cliente.setCorreo(request.getCorreo());
        cliente.setTelefono(request.getTelefono());

        return ClienteMapper.toResponse(repository.save(cliente));
    }

    public void eliminar(Integer id) {
        repository.deleteById(id);
    }
}