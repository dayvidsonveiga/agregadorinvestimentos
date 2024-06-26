package br.com.agregadorinvestimentos.service;

import br.com.agregadorinvestimentos.dto.CreateStockDto;
import br.com.agregadorinvestimentos.entity.Stock;
import br.com.agregadorinvestimentos.repository.StockRepository;
import org.springframework.stereotype.Service;

@Service
public class StockService {

    private final StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public void createStock(CreateStockDto createStockDto) {
        var stock = new Stock(
                createStockDto.stockId(),
                createStockDto.description()
        );

        stockRepository.save(stock);
    }
}
