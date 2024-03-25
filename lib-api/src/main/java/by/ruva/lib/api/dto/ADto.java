package by.ruva.lib.api.dto;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
public abstract class ADto {

    @Id
    @Getter
    @Setter
    protected Long id;
}
