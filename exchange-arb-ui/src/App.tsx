import { useState, useEffect } from "react";
import "./App.css";
import { Quote } from "./models/Quote";

function App() {
  const [orderBook, setOrderBook] = useState<{
    bids: { price: string; quote: Quote }[];
    asks: { price: string; quote: Quote }[];
  }>({
    bids: [],
    asks: [],
  });

  useEffect(() => {
    const intervalId = setInterval(() => {
      fetchOrderBook();
    }, 1000);

    return () => clearInterval(intervalId);
  }, []);

  async function fetchOrderBook() {
    try {
      const response = await fetch("http://localhost:8080/mexc/order-book");

      if (!response.ok) {
        throw new Error("Failed to fetch order book");
      }

      const data = await response.json();
      const newOrderBook: {
        bids: { price: string; quote: Quote }[];
        asks: { price: string; quote: Quote }[];
      } = { bids: [], asks: [] };
      for (const price in data.BTCUSDT.bids) {
        newOrderBook.bids.push({
          price: price,
          quote: {
            quantity: data.BTCUSDT.bids[price].quantity,
            updateTimestamp: data.BTCUSDT.bids[price].updateTimestamp,
            seqId: data.BTCUSDT.bids[price].seqId,
          },
        });
      }
      for (const price in data.BTCUSDT.asks) {
        newOrderBook.asks.push({
          price: price,
          quote: {
            quantity: data.BTCUSDT.asks[price].quantity,
            updateTimestamp: data.BTCUSDT.asks[price].updateTimestamp,
            seqId: data.BTCUSDT.asks[price].seqId,
          },
        });
      }
      setOrderBook(newOrderBook);
    } catch (error) {
      console.log(error);
    }
  }

  return (
    <div className="order-book-container">
      <div className="quotes-container">
        <div className="quotes-price-container">
          <div className="quotes-header-container">Price(USDT)</div>
          {orderBook.asks
            .slice(0, 5)
            .reverse()
            .map((ask, index) => (
              <div key={index} className="quotes-price-row ask">
                {ask.price}
              </div>
            ))}
        </div>
        <div className="quotes-size-container">
          <div className="quotes-header-container">Size(BTC)</div>
          {orderBook.asks
            .slice(0, 5)
            .reverse()
            .map((ask, index) => (
              <div key={index} className="quotes-size-row">
                {ask.quote.quantity}
              </div>
            ))}
        </div>
      </div>
      <div className="quotes-container">
        <div className="quotes-price-container">
          {orderBook.bids.slice(0, 5).map((bid, index) => (
            <div key={index} className="quotes-price-row bid">
              {bid.price}
            </div>
          ))}
        </div>
        <div className="quotes-size-container">
          {orderBook.bids.slice(0, 5).map((bid, index) => (
            <div key={index} className="quotes-size-row">
              {bid.quote.quantity}
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}

export default App;
