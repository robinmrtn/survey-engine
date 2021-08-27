import "./App.css";
import SurveyList from "./components/SurveyList";
import {BrowserRouter, Route, Switch} from "react-router-dom"
import Survey from "./components/Survey";

function App() {
    return (
        <div className="App">
            <BrowserRouter>
                <Switch>
                    <Route path='/survey/:surveyId'>
                        <Survey/>
                    </Route>
                    <Route path='/surveys'>
                        <SurveyList/>
                    </Route>
                    <Route path='/'>
                        <h1>Landing site</h1>
                    </Route>
                </Switch>
            </BrowserRouter>
        </div>
    );
}

export default App;
