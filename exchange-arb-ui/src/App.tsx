import "./App.css";
import OrderBook from "./components/order.book";

function App() {
  return (
    <div className="flex gap-10 p-2">
      <OrderBook
        exchangeName="MEXC"
        endpoint="http://localhost:8080/mexc/order-book"
        updateInterval={500}
      />
      <OrderBook
        exchangeName="Bybit"
        endpoint="http://localhost:8080/bybit/order-book"
        updateInterval={500}
      />
    </div>
  );
}

export default App;
