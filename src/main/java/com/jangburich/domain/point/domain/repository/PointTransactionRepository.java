
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PointTransactionRepository extends JpaRepository<PointTransaction, Long> {
    List<PointTransaction> findByUser(User user);

    List<StoreChargeHistoryResponse> findAllByStore(Store store);

    List<PointTransaction> findAllByStoreIdOrderByIdDesc(Long store_id);

}