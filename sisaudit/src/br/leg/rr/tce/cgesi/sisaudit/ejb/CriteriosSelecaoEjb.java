package br.leg.rr.tce.cgesi.sisaudit.ejb;

import java.io.Serializable;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import br.leg.rr.tce.cgesi.sisaudit.entity.CriteriosSelecao;

@Stateless
public class CriteriosSelecaoEjb extends AbstractEjb implements Serializable {
	private static final long serialVersionUID = 1L;
	@PersistenceContext
	private EntityManager entityManager;
	
	public void salvar(CriteriosSelecao entity) {
		try {
			entityManager.persist(entity);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}	
	}

	public void editar(CriteriosSelecao entity) {
		entityManager.merge(entity);
	}

	public void excluir(CriteriosSelecao entity) {
		entityManager.remove(entityManager.merge(entity));
	}
	
	/*
	public List<CriteriosSelecao> findAll() {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<CriteriosSelecao> criteriaQuery = criteriaBuilder
				.createQuery(CriteriosSelecao.class);
		TypedQuery<CriteriosSelecao> query = entityManager.createQuery(criteriaQuery);
		return query.getResultList();
	}
*/
		
	
}
