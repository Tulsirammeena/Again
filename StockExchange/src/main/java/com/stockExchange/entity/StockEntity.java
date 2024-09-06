package com.stockExchange.entity;

import com.stockExchange.dto.StockModel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor	
@Data
@Table(name = "stock_info")
public class StockEntity {

//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	private Integer companyId;
	
	@Column(nullable = false, length = 100)
    private String name;
	
	@Id
//	@Column(nullable = false, unique = true, length = 10)
    private String symbol;
	
	@Column(columnDefinition = "TEXT")
    private String description;
	
//	@OneToMany(mappedBy = "stock", fetch = FetchType.LAZY,cascade = CascadeType.PERSIST,
//			targetEntity = StockHistoryEntity.class)
//	private List<StockHistoryEntity> stockHistory;
    
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        CompanyEntity company = (CompanyEntity) o;
//        return Objects.equals(companyId, company.companyId) &&
//               Objects.equals(name, company.name) &&
//               Objects.equals(symbol, company.symbol) &&
//               Objects.equals(description, company.description);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(companyId, name, symbol, description);
//    }
//    
    @Override
    public String toString() {
        return "Company{" +
               "name='" + name + '\'' +
               ", symbol='" + symbol + '\'' +
               ", description='" + description + '\'' +
               ""+
               '}';
    }
    
//   public StockEntity(StockModel stock) {
//	   this.name = stock.getName();
//	   this.symbol = stock.getSymbol();
//	   this.description = stock.getDescription();
//   }
	
}
