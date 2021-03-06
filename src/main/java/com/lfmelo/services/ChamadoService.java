package com.lfmelo.services;

import java.time.LocalDate;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lfmelo.domain.Chamado;
import com.lfmelo.domain.Cliente;
import com.lfmelo.domain.Tecnico;
import com.lfmelo.dtos.ChamadoDTO;
import com.lfmelo.enums.EPrioridade;
import com.lfmelo.enums.EStatus;
import com.lfmelo.exceptions.NotFoundException;
import com.lfmelo.repositories.ChamadoRepository;

@Service
public class ChamadoService {

	@Autowired
	private ChamadoRepository repository;
	
	@Autowired
	private ClienteService clienteService;
	
	@Autowired
	private TecnicoService tecnicoService;
	
	public Chamado findById(Integer id) {
		return this.repository.findById(id).orElseThrow(() -> new NotFoundException("Objeto não encontrado id " + id));
	}
	
	public List<Chamado> findAll() {
		return this.repository.findAll();
	}

	public Chamado create(ChamadoDTO dto) {
		return this.repository.save(newChamadoFromDto(dto));
	}
	
	public Chamado newChamadoFromDto(ChamadoDTO dto) {
		Chamado chamado = new Chamado();
		Cliente cliente = this.clienteService.findById(dto.getCliente());
		Tecnico tecnico = this.tecnicoService.findById(dto.getTecnico());
		
		if(dto.getId() != null) {
			chamado.setId(dto.getId());
		}
		
		if(dto.getStatus().equals(2)) {
			chamado.setDataFechamento(LocalDate.now());
		}
		
		chamado.setCliente(cliente);
		chamado.setTecnico(tecnico);
		chamado.setPrioridade(EPrioridade.toEnum(dto.getPrioridade()));
		chamado.setStatus(EStatus.toEnum(dto.getStatus()));
		chamado.setObservacoes(dto.getObservacoes());
		chamado.setTitulo(dto.getTitulo());
		
		return chamado;
	}

	public Chamado update(@Valid ChamadoDTO dto, Integer id) {
			dto.setId(id);
			Chamado oldObj = findById(id);
			oldObj = newChamadoFromDto(dto);
			return repository.save(oldObj);
	}

	public void delete(Integer id) {
		Chamado chamado = this.findById(id);
		this.repository.delete(chamado);
		
	}
	
	
}
