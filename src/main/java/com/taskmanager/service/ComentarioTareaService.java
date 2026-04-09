package com.taskmanager.service;

import com.taskmanager.dto.ComentarioTareaDTO;
import com.taskmanager.exception.ResourceNotFoundException;
import com.taskmanager.model.ComentarioTarea;
import com.taskmanager.model.Tarea;
import com.taskmanager.model.Usuario;
import com.taskmanager.repository.ComentarioTareaRepository;
import com.taskmanager.repository.TareaRepository;
import com.taskmanager.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ComentarioTareaService {

    @Autowired
    private ComentarioTareaRepository comentarioTareaRepository;

    @Autowired
    private TareaRepository tareaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<ComentarioTareaDTO> findByTareaId(Long tareaId) {
        return comentarioTareaRepository.findByTareaIdOrderByFechaCreacionDesc(tareaId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ComentarioTareaDTO create(Long tareaId, ComentarioTareaDTO dto, String emailAutor) {
        Tarea tarea = tareaRepository.findById(tareaId)
                .orElseThrow(() -> new ResourceNotFoundException("Tarea", tareaId));

        Usuario autor = usuarioRepository.findByEmail(emailAutor)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        ComentarioTarea comentario = new ComentarioTarea();
        comentario.setContenido(dto.getContenido());
        comentario.setTarea(tarea);
        comentario.setAutor(autor);

        ComentarioTarea saved = comentarioTareaRepository.save(comentario);
        return convertToDTO(saved);
    }

    public ComentarioTareaDTO update(Long id, ComentarioTareaDTO dto) {
        ComentarioTarea comentario = comentarioTareaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ComentarioTarea", id));

        comentario.setContenido(dto.getContenido());

        ComentarioTarea updated = comentarioTareaRepository.save(comentario);
        return convertToDTO(updated);
    }

    public void delete(Long id) {
        if (!comentarioTareaRepository.existsById(id)) {
            throw new ResourceNotFoundException("ComentarioTarea", id);
        }
        comentarioTareaRepository.deleteById(id);
    }

    private ComentarioTareaDTO convertToDTO(ComentarioTarea comentario) {
        return new ComentarioTareaDTO(
                comentario.getId(),
                comentario.getContenido(),
                comentario.getTarea().getId(),
                comentario.getAutor().getId(),
                comentario.getAutor().getNombre() + " " + comentario.getAutor().getApellido(),
                comentario.getAutor().getApodo(),
                comentario.getFechaCreacion()
        );
    }
}
