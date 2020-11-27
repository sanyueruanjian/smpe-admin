package marchsoft.base;

import java.util.List;

/**
 * java struct mapper base class.To transfer between Dto and Entity.
 *
 * @author Wangmingcan
 * @date 2020/8/18 8:31
 */
public interface BaseMapStruct<D, E> {
    /**
     * DTO转Entity
     *
     * @param dto /
     * @return /
     */
    E toEntity(D dto);

    /**
     * Entity转DTO
     *
     * @param entity /
     * @return /
     */
    D toDto(E entity);

    /**
     * DTO集合转Entity集合
     *
     * @param dtoList /
     * @return /
     */
    List<E> toEntity(List<D> dtoList);

    /**
     * Entity集合转DTO集合
     *
     * @param entityList /
     * @return /
     */
    List<D> toDto(List<E> entityList);
}
