package com.codmind.orderapi.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "ORDERS")
public class Order {

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="REG_DATE", nullable = false, updatable = false)
	private LocalDateTime regDate;
	
	//cascade -> para que cuando se borre una order borre sus orderLine
	//fetch -> para que cuando cargue una orden traiga tambien sus orderLine con EAGER
	@OneToMany(mappedBy = "order", cascade=CascadeType.ALL, fetch = FetchType.EAGER)
	private List<OrderLine> lines;
	
	@Column(name="TOTAL", nullable = false)
	private Double total;

	@ManyToOne
	@JoinColumn(name="FK_USER", updatable = false)
	private User user;
	
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Order other = (Order) obj;
		return Objects.equals(id, other.id);
	}

	
}
