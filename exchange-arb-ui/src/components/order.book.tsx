import { useState, useEffect } from "react";
import { Quote } from "../models/Quote";

interface Props {
  exchangeName: String;
  endpoint: RequestInfo;
  updateInterval?: number;
}

export default function OrderBook({
  exchangeName,
  endpoint,
  updateInterval = 1000,
}: Props) {
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
    }, updateInterval);

    return () => clearInterval(intervalId);
  }, []);

  async function fetchOrderBook() {
    try {
      const response = await fetch(endpoint);

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
    <div className="flex flex-col p-2 gap-1 w-60 border-2 rounded">
      <div className="text-center font-semibold text-xl">{exchangeName}</div>
      <div className="flex text-sky-300 text-left">
        <div className="flex-1">Price(USDT)</div>
        <div className="flex-1">Size(BTC)</div>
      </div>
      <div className="flex flex-col text-left">
        {orderBook.asks
          .slice(0, 5)
          .reverse()
          .map((ask, index) => (
            <div key={index} className="flex">
              <div className="flex-1 text-red-600">{ask.price}</div>
              <div className="flex-1">{ask.quote.quantity}</div>
            </div>
          ))}
      </div>
      <div className="flex flex-col text-left">
        {orderBook.bids.slice(0, 5).map((bid, index) => (
          <div key={index} className="flex">
            <div className="flex-1 text-green-600">{bid.price}</div>
            <div className="flex-1">{bid.quote.quantity}</div>
          </div>
        ))}
      </div>
    </div>
  );
}
