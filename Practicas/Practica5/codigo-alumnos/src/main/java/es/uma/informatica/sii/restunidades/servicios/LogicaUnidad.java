package es.uma.informatica.sii.restunidades.servicios;

import es.uma.informatica.sii.restunidades.dtos.UnidadDTO;
import es.uma.informatica.sii.restunidades.entidades.UnidadDocente;
import es.uma.informatica.sii.restunidades.excepciones.UnidadExistenteException;
import es.uma.informatica.sii.restunidades.excepciones.UnidadNoEncontrada;
import es.uma.informatica.sii.restunidades.repositorios.UnidadRepo;

import org.hibernate.cache.spi.support.AbstractReadWriteAccess.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LogicaUnidad {
	
	private UnidadRepo repo;
	
	@Autowired
	public LogicaUnidad(UnidadRepo repo) {
		this.repo=repo;
	}

	public List<UnidadDocente> getTodasUnidades() {
		return repo.findAll();
	}
	
	public Long aniadir(UnidadDocente u){
        if(repo.findByCursoAndGrupo(u.getCurso(), u.getGrupo()).isPresent()){
            throw new UnidadExistenteException();
        }
        repo.save(u);
        return u.getId();
    }

	public Optional<UnidadDocente> unidadId(Long id) {
		if (repo.findById(id) != null) {
			return repo.findById(id);
		}else{
			throw new UnidadNoEncontrada();
		}
	}

	public void modificarUnidad(UnidadDocente u, Long id) {
	if (repo.existsById(u.getId())) {
			// Verifica si ya existe una unidad con el mismo curso y grupo
			boolean existsWithSameCursoAndGrupo = repo.findByCursoAndGrupo(u.getCurso(), u.getGrupo()).isPresent();
	
			if (existsWithSameCursoAndGrupo && repo.findByCursoAndGrupo(u.getCurso(), u.getGrupo()).get().getId() != id) {
				// Lanza un error 403 (Forbidden) si ya existe una unidad con el mismo curso y grupo
				throw new ResponseStatusException(HttpStatus.FORBIDDEN, "La unidad ya existe con el mismo curso y grupo");
			}
	
			// Actualiza la unidad existente
			Optional<UnidadDocente> existingUnidad = repo.findById(u.getId());
			existingUnidad.ifPresent(l -> {
				l.setCurso(u.getCurso());
				l.setGrupo(u.getGrupo());
				l.setAula(u.getAula());
			});
		} else {
			// Lanza un error 404 (Not Found) si la unidad no existe
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La unidad no fue encontrada");
		}
	}

	public void eliminar(Long id) {
		if (repo.existsById(id) ){
			repo.deleteById(id);
		} else {
			throw new UnidadNoEncontrada();
		}
	}
}
