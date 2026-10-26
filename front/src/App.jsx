import { BrowserRouter, Routes, Route } from "react-router-dom";
import Header from "./components/Header/Header";
import Footer from "./components/Footer/Footer";
import Home from "./pages/Home/Home";
import Games from "./pages/Games/Games";
import Details from "./pages/Details/Details"
import Movies from "./pages/Movies/Movies"
import Series from "./pages/Series/Series";
import Login from "./pages/Login/Login"
import Sigin from "./pages/Signin/Signin"



function App() {
  return (
    <BrowserRouter>
      <div className="min-h-screen bg-[#10121A]">
        <Header />

        <main className="mx-auto max-w-7xl px-4 py-8 sm:px-6 lg:px-8">
          <Routes>
            <Route path="/" element={<Home />} />
            <Route
              path="/movies"
              element={<Movies />}
            />
            <Route 
            path="/series"
            element={<Series />}
            />
         
            <Route
              path="/games"
              element={<Games />}
            />
            <Route
              path="/detalhes/:type/:id"
              element={<Details />}
            />

             <Route
              path="/login"
              element={<Login />}
            />
         <Route
              path="/cadastro"
              element={<Sigin />}
            />
            
          </Routes>
        </main>

        <Footer />
      </div>
    </BrowserRouter>
  );
}

export default App;