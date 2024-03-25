package by.ruva.lib.api.utils;

import java.util.List;

import by.ruva.lib.api.dto.ADto;
import by.ruva.lib.entities.AEntity;

public interface IMapper<E extends AEntity, D extends ADto> {

    E toEntity(D dto);

    E toLiteEntity(D dto);

    D toDto(E entity);

    D toLiteDto(E entity);

    List<D> toListDto(List<E> entities);
}