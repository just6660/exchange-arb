import math
from scipy.stats import norm


def calculate_d2(S, K, T, r, sigma):
    # Calculate d1
    d1 = (math.log(S / K) + (r + 0.5 * sigma ** 2) * T) / (sigma * math.sqrt(T))

    # Calculate d2
    d2 = d1 - sigma * math.sqrt(T)
    return d2


# Example inputs
S = 94000  # Current stock price
K = 93000  # Strike price
T = (31-8)/365  # Time to expiration (1 year)
r = 0.05  # Risk-free interest rate (5%)
sigma = 0.5604  # Volatility (20%)

# Calculate d2
d2 = calculate_d2(S, K, T, r, sigma)
print(f"d2: {d2}")

N_d2 = norm.cdf(-d2)

print(f"N(d2): {N_d2}")
