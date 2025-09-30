import { BrowserRouter as Router, Routes, Route } from "react-router-dom";

import Footer from './Footer.jsx';
import Home from './pages/Home/Home.jsx';
import SignIn from './pages/SignIn/SignIn.jsx';
import Register from "./pages/Register/Register.jsx";
import UserProfile from "./pages/UserProfile/UserProfile.jsx";
import MovieXmlImport from "./api/MovieXmlImport.jsx";
import MovieList from "./pages/Movies/MovieList.jsx";
import MoviePage from "./pages/Movies/MoviePage.jsx";
import BookingForm from './pages/Bookings/BookingForm.jsx';
import ShowtimePage from './pages/Bookings/ShowtimePage.jsx';
import SearchResultsPage from "./pages/SearchResults/SearchResults.jsx";
import GlobalHeader from "./pages/Home/HomeHeader.jsx";

import AdminPage from "./admin/AdminPage.jsx";
import DashboardPage from "./admin/DashBoard/DashboardPage.jsx";
import MovieAdminPage from "./admin/MovieAdminPage.jsx";
import ShowtimeAdminPage from "./admin/ShowtimesAdminPage.jsx";
import UsersAdminPage from "./admin/UsersAdminPage.jsx";

import AdminRoute from './admin/AdminRoute';
import Unauthorized from './pages/Unauthorized';
import ContactUs from "./pages/FooterLinks/ContactUs.jsx";
import TermsOfService from "./pages/FooterLinks/TermsOfService.jsx";
import PrivacyPolicy from "./pages/FooterLinks/PrPolicy.jsx";

function App() {
  return (
    <Router>
      <div className="app-container">
        <Routes>
          {/* Public routes */}
          <Route path="/" element={<Home />} />
          <Route path="/sign-in" element={<SignIn />} />
          <Route path="/register" element={<Register />} />
          <Route path="/movies" element={<MovieList />} />
          <Route path="/movies/:id" element={<MoviePage />} />
          <Route path="/search" element={<SearchResultsPage />} />
          <Route path="/users/profile/:userId/:section" element={<UserProfile />} />
          <Route path="/users/profile/:userId" element={<UserProfile />} />
          <Route path="/showtimes/:showtimeId" element={<ShowtimePage />} />
          <Route path="/booking/:showtimeId" element={<BookingForm />} />
          <Route path="/unauthorized" element={<Unauthorized />} />


          {/* Admin layout with nested routes */}
        <Route element={<AdminRoute />}>
          <Route path="/admin" element={<AdminPage />}>
            <Route index element={<DashboardPage />} /> {/* default */}
            <Route path="dashboard" element={<DashboardPage />} />
            <Route path="movies" element={<MovieAdminPage />} />
            <Route path="import" element={<MovieXmlImport />} />
            <Route path="showtimes/all" element={<ShowtimeAdminPage />} />
            <Route path="users" element={<UsersAdminPage />} />
          </Route>
          </Route >

          {/*Footer Routes*/}
          <Route path= "/contact" element = {<ContactUs/>} ></Route>
          <Route path= "/terms" element = {<TermsOfService/>} ></Route> 
          <Route path="/pr-policy"  element = {<PrivacyPolicy/>}> </Route>
        </Routes>
        <Footer />
      </div>
    </Router>
  );
}

export default App;
