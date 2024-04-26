package es.uma.informatica.sii.restunidades.controladores;

import es.uma.informatica.sii.restunidades.dtos.UnidadDTO;
import es.uma.informatica.sii.restunidades.dtos.UnidadNuevaDTO;
import es.uma.informatica.sii.restunidades.entidades.UnidadDocente;
import es.uma.informatica.sii.restunidades.excepciones.UnidadExistenteException;
import es.uma.informatica.sii.restunidades.excepciones.UnidadNoEncontrada;
import es.uma.informatica.sii.restunidades.servicios.LogicaUnidad;

import org.hibernate.cache.spi.support.AbstractReadWriteAccess.Item;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/unidad")
public class ControladorRest {
	private LogicaUnidad servicio;

	public ControladorRest(LogicaUnidad servicioUnidad) {
		servicio = servicioUnidad;
	}

	@GetMapping
	public ResponseEntity<List<UnidadDTO>> listaDeUnidades() {
		return ResponseEntity.ok(servicio.getTodasUnidades().stream()
				.map(Mapper::toUnidadDTO)
				.toList());
	}

	@PostMapping
    public ResponseEntity<UnidadDTO> aniadirUnidad(@RequestBody UnidadNuevaDTO unidad, UriComponentsBuilder builder) {

        UnidadDocente u2 = Mapper.toUnidad(unidad);

        try{
            Long id = servicio.aniadir(u2);
            URI uri = builder
                .path("/api")
                .path("/v1")
                .path("/unidad")
                .path(String.format("/%d", id))
                .build()
                .toUri();
            ResponseEntity.ok();
            return ResponseEntity.created(uri).build();
        }catch( UnidadExistenteException e ){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "La unidad ya existe con el mismo curso y grupo");
        }
    }

	@GetMapping("{id}")
	public ResponseEntity<UnidadDTO> obtenerPorId(@PathVariable Long id) {
		return ResponseEntity.of(servicio.unidadId(id).map(Mapper::toUnidadDTO));
	}

	@PutMapping("{id}")
	public ResponseEntity<?> modificaUnidad(@PathVariable Long id, @RequestBody UnidadDocente u) {
		u.setId(id);
		servicio.modificarUnidad(u, id);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public void eliminar(@PathVariable Long id) {
		try {
			servicio.eliminar(id);
		} catch (UnidadNoEncontrada e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La unidad no fue encontrada", e);
		}
	}
}
